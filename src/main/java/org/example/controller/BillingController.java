package org.example.controller;

import org.example.model.Billing;
import org.example.model.TravelRecord;
import org.example.model.Invoice;
import org.example.model.Booking;
import org.example.repository.BillingRepository;
import org.example.repository.TravelRecordRepository;
import org.example.repository.InvoiceRepository;
import org.example.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class BillingController {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private TravelRecordRepository travelRecordRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/")
    public String showForm() {
        return "billing";
    }

    @PostMapping("/calculate")
    public String calculateBill(
            @RequestParam String customerName,
            @RequestParam String mobileNumber,
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam double distance,
            @RequestParam double pricePerKm,
            Model model) {

        double totalPrice = distance * pricePerKm;

        // Generate unique billing number
        String billingNumber = generateBillingNumber();

        // Save to all 4 tables
        Billing billing = new Billing(billingNumber, customerName, mobileNumber, source, destination,
                distance, pricePerKm, totalPrice);
        billingRepository.save(billing);

        TravelRecord travelRecord = new TravelRecord(billingNumber, customerName, mobileNumber, source, destination,
                distance, pricePerKm, totalPrice);
        travelRecordRepository.save(travelRecord);

        Invoice invoice = new Invoice(billingNumber, customerName, mobileNumber, source, destination,
                distance, pricePerKm, totalPrice);
        invoiceRepository.save(invoice);

        Booking booking = new Booking(billingNumber, customerName, mobileNumber, source, destination,
                distance, pricePerKm, totalPrice);
        bookingRepository.save(booking);

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));

        model.addAttribute("billingNumber", billingNumber);
        model.addAttribute("customerName", customerName);
        model.addAttribute("mobileNumber", mobileNumber);
        model.addAttribute("source", source);
        model.addAttribute("destination", destination);
        model.addAttribute("distance", distance);
        model.addAttribute("pricePerKm", pricePerKm);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("currentDate", currentDate);
        model.addAttribute("currentTime", currentTime);
        model.addAttribute("receiptId", billing.getId());

        return "billing";
    }

    private String generateBillingNumber() {
        Long maxId = billingRepository.findMaxId();
        long nextNumber = (maxId == null) ? 1 : maxId + 1;
        return String.format("BILL-%06d", nextNumber);
    }

    @GetMapping("/customers")
    public String viewAllCustomers(Model model) {
        List<Billing> billings = billingRepository.findAllByOrderByCreatedAtDesc();
        model.addAttribute("customers", billings);
        
        // Calculate total revenue
        double totalRevenue = billings.stream()
                .mapToDouble(b -> b.getTotalPrice() != null ? b.getTotalPrice() : 0.0)
                .sum();
        model.addAttribute("totalRevenue", totalRevenue);
        
        return "customers";
    }
}

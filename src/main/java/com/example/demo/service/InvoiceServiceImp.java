package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.InvoiceNotFoundException;
import com.example.demo.Repository.InvoiceRepository;
import com.example.demo.entity.Invoice;

@Service

public class InvoiceServiceImp implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepo;
    
    Logger log = LoggerFactory.getLogger(InvoiceServiceImp.class);

    @Override
    public Invoice saveInvoice(Invoice inv) {

        return invoiceRepo.save(inv);
    }

    @Override
    @CachePut(value="Invoice", key="#invId")
    public Invoice updateInvoice(Invoice inv, Integer invId) {
       Invoice invoice = invoiceRepo.findById(invId)
            .orElseThrow(() -> new InvoiceNotFoundException("Invoice Not Found"));
       invoice.setInvAmount(inv.getInvAmount());
       invoice.setInvName(inv.getInvName());
       return invoiceRepo.save(invoice);
    }

    @Override
    @CacheEvict(value="Invoice", key="#invId")
    // @CacheEvict(value="Invoice", allEntries=true) //in case there are multiple records to delete
    public void deleteInvoice(Integer invId) {
       Invoice invoice = invoiceRepo.findById(invId)
           .orElseThrow(() -> new InvoiceNotFoundException("Invoice Not Found"));
       invoiceRepo.delete(invoice);
    }

    @Override
    @Cacheable(value="Invoice", key="#invId")
    public Invoice getOneInvoice(Integer invId) {
       Invoice invoice = invoiceRepo.findById(invId)
         .orElseThrow(() -> new InvoiceNotFoundException("Invoice Not Found"));
       return invoice;
    }

    @Override
    @Cacheable(value="Invoice")
    public List<Invoice> getAllInvoices() {
    	log.info("From the service layer");
       return invoiceRepo.findAll();
    }
}
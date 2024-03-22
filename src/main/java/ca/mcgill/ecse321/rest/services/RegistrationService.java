package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
    @Service
    public class RegistrationService {

        @Autowired
        RegistrationRepository registrationRepository;
        @Autowired
        InvoiceRepository invoiceRepository;
        @Autowired
        InstructorRepository instructorRepository;
        @Autowired
        PersonRepository personRepository;
        @Autowired
        CourseRepository courseRepository;
        @Autowired
        CustomerRepository customerRepository;


        /** Create a registration between a customer and a course and save it.
         **/

        public Registration register(Customer customer, Course course) {

            if (customer == null || course == null) {
                throw new IllegalArgumentException("Customer or Course cannot be empty!");
            }

            Registration registration = new Registration();
            registration.setCustomer(customer);
            registration.setCourse(course);

            registrationRepository.save(registration);

            return registration;

        }


        //Get all registrations GET /registrations

        //Owner, can see all registrations
        //Get all registrations

        public List<Registration> getAllRegistrations(){
            List<Registration> registrations = toList(registrationRepository.findAll());


            return registrations;
        }


        //Instructor can see registrations for their courses
        //Get registrations for a certain course, for one instructor


        public List<Registration> getRegistrationsForInstructor(String instructorID){
            if (instructorID == null || instructorID.trim().isEmpty()) {
                throw new IllegalArgumentException("InstructorID cannot be empty!");
            }

            Instructor instructor = instructorRepository.findInstructorById(instructorID);

            List<Registration> allRegistrations = getAllRegistrations();
            List<Registration> registrationsByInstructor = new ArrayList<>();



            for (Registration r : allRegistrations) {

                if (r.getCourse().getInstructor().equals(instructor)){
                    registrationsByInstructor.add(r);
                }
            }



            return registrationsByInstructor;
        }


        //Customer can see their own registrations
        //get registration for one customer

        public List<Registration> getRegistrationsForCustomer(String customerID) {

            if (customerID == null || customerID.trim().isEmpty()) {
                throw new IllegalArgumentException("CustomerID cannot be empty!");
            }

            Customer customer = customerRepository.findCustomerById(customerID);
            List<Registration> allRegistrations = getAllRegistrations();
            List<Registration> registrationsForCustomer = new ArrayList<>();



            for (Registration r : allRegistrations) {
                if (r.getCustomer().equals(customer)) {
                    registrationsForCustomer.add(r);
                }
            }


            return registrationsForCustomer;

        }

        @Transactional
        public List<Registration> getRegistrations(PersonSession personSession) {

            if (personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
                return getAllRegistrations();
            }

            if (personSession.getPersonType().equals(PersonSession.PersonType.Instructor)) {
                return getRegistrationsForInstructor(personSession.getPersonId());
            }

            if (personSession.getPersonType().equals(PersonSession.PersonType.Customer)) {
                return getRegistrationsForCustomer(personSession.getPersonId());
            } else {
                throw new IllegalArgumentException("Invalid credentials");
            }
        }

        //Get a specific registration GET /registrations/{registration_id}
        //Same conditions as 5.a

        public Registration getSpecificRegistrationByID(String registrationID) {
            if (registrationID == null || registrationID.trim().isEmpty()) {
                throw new IllegalArgumentException("RegistrationID cannot be empty!");
            }

            List<Registration> allRegistrations = getAllRegistrations();
            for (Registration r : allRegistrations) {
                if (r.getId().equals(registrationID)) {
                    return r;
                }
            }
            return null;
        }


        public Registration getSpecificRegistrationByIDForInstructor(String instructorID, String registrationID) {
            if (registrationID == null || registrationID.trim().isEmpty()) {
                throw new IllegalArgumentException("RegistrationID cannot be empty!");
            }
            if (instructorID == null || instructorID.trim().isEmpty()) {
                throw new IllegalArgumentException("InstructorID cannot be empty!");
            }

            List<Registration> instructorRegistrations = getRegistrationsForInstructor(instructorID);
            for (Registration r : instructorRegistrations) {
                if (r.getId().equals(registrationID)) {
                    return r;
                }
            }
            return null;
        }


        public Registration getSpecificRegistrationByIDForCustomer(String customerID, String registrationID) {
            if (registrationID == null || registrationID.trim().isEmpty()) {
                throw new IllegalArgumentException("RegistrationID cannot be empty!");
            }
            if (customerID == null || customerID.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer cannot be empty!");
            }

            List<Registration> customerRegistrations = getRegistrationsForCustomer(customerID);
            for (Registration r : customerRegistrations) {
                if (r.getId().equals(registrationID)) {
                    return r;
                }
            }
            return null;
        }

        @Transactional
        public Registration getSpecificRegistration(PersonSession personSession, String registration_id){
            if (personSession == null ) {
                throw new IllegalArgumentException("personSession cannot be null!");
            }

            if(personSession.getPersonType().equals(PersonSession.PersonType.Owner)){
                return getSpecificRegistrationByID(registration_id);
            }

            if(personSession.getPersonType().equals(PersonSession.PersonType.Instructor)){
                return getSpecificRegistrationByIDForInstructor(personSession.getPersonId(), registration_id);
            }

            if(personSession.getPersonType().equals(PersonSession.PersonType.Customer)){
                return getSpecificRegistrationByIDForCustomer(personSession.getPersonId(), registration_id);
            }
            else {
                throw new IllegalArgumentException("Invalid credentials or registrationID");
            }

        }

        //Cancel a specific registration POST /registrations/{registration_id}/cancel
        //Owner can cancel any registration
        //Customer can cancel their registrations

        public boolean cancelRegistration(String registrationID) {
            if (registrationID == null || registrationID.trim().isEmpty()) {
                throw new IllegalArgumentException("RegistrationID cannot be empty!");
            }
            for (Registration r : registrationRepository.findAll()) {
                if (r.getId().equals(registrationID)){
                    r.delete();
                    return true;
                }
            }
            return false;
        }


        public boolean cancelRegistrationByCustomer(String customerID, String  registrationID) {
            if (registrationID == null || registrationID.trim().isEmpty()) {
                throw new IllegalArgumentException("RegistrationID cannot be empty!");
            }
            if (customerID == null || customerID.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer cannot be empty!");
            }
            List<Registration> customerRegistrations = getRegistrationsForCustomer(customerID);
            for (Registration r : customerRegistrations) {
                if (r.getId().equals(registrationID)){
                    r.delete();
                    return true;
                }
            }
            return false;
        }

        @Transactional
        public boolean cancelSpecificRegistration(PersonSession personSession, String registration_id){
            if(personSession.getPersonType().equals(PersonSession.PersonType.Owner)){
                return cancelRegistration(registration_id);
            }

            if(personSession.getPersonType().equals(PersonSession.PersonType.Customer)){
                return cancelRegistrationByCustomer(personSession.getPersonId(), registration_id);
            }
            else {
                throw new IllegalArgumentException("Invalid credentials or registrationID");
            }

        }

        //Update registration (for course rating) POST /registrations/{registration_id}/rating
        //Only customers can do it and once its done it cannot be changed
        @Transactional
        public boolean updateRegistrationRating(PersonSession personSession, String registrationID, Integer rating) {
            if (registrationID == null || registrationID.trim().isEmpty()) {
                throw new IllegalArgumentException("RegistrationID cannot be empty!");
            }
            if (rating <1 || rating > 5) {
                throw new IllegalArgumentException("Invalid rating");
            }

            if(personSession.getPersonType().equals(PersonSession.PersonType.Customer)){
            for (Registration r : registrationRepository.findAll()) {
                if (r.getId().equals(registrationID) && r.getCustomer().getId().equals(personSession.getPersonId())){
                    r.setRating(rating);
                    return true;
                };
            }}
            return false;
        }

        //View invoices for a specific registration GET /registrations/{registration_id}/invoices
        //ID + amount

        public List<Invoice> getInvoicesForRegistration(String registrationID) {

            if (registrationID == null || registrationID.trim().isEmpty()) {
                throw new IllegalArgumentException("RegistrationID cannot be empty!");
            }

            List<Invoice> allInvoices = toList(invoiceRepository.findAll());
            Registration registration= registrationRepository.findRegistrationById(registrationID);
            List<Invoice> invoiceForRegistration = new ArrayList<>();

            for (Invoice i : allInvoices) {
                if (i.getRegistration().equals(registration)) {
                    invoiceForRegistration.add(i);
                }
            }
            return invoiceForRegistration;
        }


        public List<Invoice> getInvoicesForCustomerRegistration(String customerID, String registrationID) {
            if (registrationID == null || registrationID.trim().isEmpty()) {
                throw new IllegalArgumentException("RegistrationID cannot be empty!");
            }
            if (customerID == null || customerID.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer cannot be empty!");
            }
            Customer customer = customerRepository.findCustomerById(customerID);
            List<Invoice> invoicesForRegistrationID = getInvoicesForRegistration(registrationID);
            List<Invoice> invoiceForRegistration = new ArrayList<>();

            for (Invoice i : invoicesForRegistrationID) {
                if (i.getRegistration().getCustomer().equals(customer)) {
                    invoiceForRegistration.add(i);
                }
            }
            System.out.println(invoiceForRegistration);
            return invoiceForRegistration;
        }


        @Transactional
        public List<Invoice> getInvoices(PersonSession personSession, String registration_id){
            if(personSession.getPersonType().equals(PersonSession.PersonType.Owner)){
                return getInvoicesForRegistration(registration_id);
            }

            if(personSession.getPersonType().equals(PersonSession.PersonType.Customer)){
                return getInvoicesForCustomerRegistration(personSession.getPersonId(), registration_id);
            }
            else {
                throw new IllegalArgumentException("Invalid credentials or registrationID");
            }
        }

        private <T> List<T> toList(Iterable<T> iterable){
            List<T> resultList = new ArrayList<T>();
            for (T t : iterable) {
                resultList.add(t);
            }
            return resultList;
        }
    }


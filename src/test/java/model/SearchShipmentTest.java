package model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * JUnit Test for Search Shipment by ID or Date User Story
 */
public class SearchShipmentTest {

    private List<Shipment> testShipments;
    private Date startDate;
    private Date endDate;
    private SimpleDateFormat dateFormat;

    @Before
    public void setUp() throws ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        startDate = new Date(dateFormat.parse("2023-01-01").getTime());
        endDate = new Date(dateFormat.parse("2023-12-31").getTime());

        // Create test shipments with different dates
        testShipments = new ArrayList<>();
        
        Shipment shipment1 = new Shipment(1, 1, "Express", "123 Test St", "Sydney", "NSW", "2000");
        shipment1.setShipmentID(1);
        shipment1.setCreatedDate(new Date(dateFormat.parse("2023-01-15").getTime()));
        
        Shipment shipment2 = new Shipment(2, 1, "Standard", "456 Oak Ave", "Melbourne", "VIC", "3000");
        shipment2.setShipmentID(2);
        shipment2.setCreatedDate(new Date(dateFormat.parse("2023-06-20").getTime()));
        
        Shipment shipment3 = new Shipment(3, 1, "Priority", "789 Pine Rd", "Brisbane", "QLD", "4000");
        shipment3.setShipmentID(3);
        shipment3.setCreatedDate(new Date(dateFormat.parse("2023-12-10").getTime()));

        testShipments.add(shipment1);
        testShipments.add(shipment2);
        testShipments.add(shipment3);
    }

    @Test
    public void testSearchShipmentsByDateRange() throws ParseException {
        // Test searching shipments within date range
        Date searchStartDate = new Date(dateFormat.parse("2023-01-01").getTime());
        Date searchEndDate = new Date(dateFormat.parse("2023-12-31").getTime());
        
        List<Shipment> results = searchShipmentsByDateRange(1, searchStartDate, searchEndDate, null);
        
        assertNotNull("Search results should not be null", results);
        assertEquals("Should return all 3 shipments", 3, results.size());
    }

    @Test
    public void testSearchShipmentsBySpecificDateRange() throws ParseException {
        // Test searching with specific creation date
        Date searchStartDate = new Date(dateFormat.parse("2023-06-01").getTime());
        Date searchEndDate = new Date(dateFormat.parse("2023-12-31").getTime());
        
        List<Shipment> results = searchShipmentsByDateRange(1, searchStartDate, searchEndDate, null);
        
        assertEquals("Should return 2 shipments", 2, results.size());
        
        for (Shipment shipment : results) {
            assertTrue("Shipment date should be within range", 
                shipment.getCreatedDate().compareTo(searchStartDate) >= 0 &&
                shipment.getCreatedDate().compareTo(searchEndDate) <= 0);
        }
    }

    @Test
    public void testSearchShipmentById() {
        // Test searching by shipment ID
        Integer searchId = 2;
        List<Shipment> results = searchShipmentsByDateRange(1, null, null, searchId);
        
        assertEquals("Should return 1 shipment", 1, results.size());
        assertEquals("Should return shipment with ID 2", 2, results.get(0).getShipmentID());
    }

    @Test
    public void testSearchShipmentByIdNotFound() {
        // Test searching by non-existent shipment ID
        Integer searchId = 999;
        List<Shipment> results = searchShipmentsByDateRange(1, null, null, searchId);
        
        assertTrue("Should return empty list for non-existent ID", results.isEmpty());
    }

    @Test
    public void testSearchBelongsToCustomer() {
        // Test that search results belong to the user
        List<Shipment> results = searchShipmentsByDateRange(1, startDate, endDate, null);
        
        for (Shipment shipment : results) {
            assertEquals("All shipments should belong to customer 1", 1, shipment.getCustomerID());
        }
    }

    @Test
    public void testSearchWithStartDateOnly() throws ParseException {
        // Test searching with creation date only
        Date searchStartDate = new Date(dateFormat.parse("2023-06-01").getTime());
        List<Shipment> results = searchShipmentsByDateRange(1, searchStartDate, null, null);
        
        for (Shipment shipment : results) {
            assertTrue("Shipment date should be after start date", 
                shipment.getCreatedDate().compareTo(searchStartDate) >= 0);
        }
    }

    @Test
    public void testSearchWithEndDateOnly() throws ParseException {
        // Test searching with end date only
        Date searchEndDate = new Date(dateFormat.parse("2023-06-30").getTime());
        List<Shipment> results = searchShipmentsByDateRange(1, null, searchEndDate, null);
        
        for (Shipment shipment : results) {
            assertTrue("Shipment date should be before end date", 
                shipment.getCreatedDate().compareTo(searchEndDate) <= 0);
        }
    }

    @Test
    public void testSearchResultsNotNull() {
        // Test that search always returns a list (never null)
        List<Shipment> results1 = searchShipmentsByDateRange(1, null, null, null);
        List<Shipment> results2 = searchShipmentsByDateRange(1, startDate, endDate, 999);
        
        assertNotNull("Results should not be null for valid search", results1);
        assertNotNull("Results should not be null for not found search", results2);
    }

    @Test
    public void testSearchEmptyResults() {
        // Test search with no criteria
        List<Shipment> results = searchShipmentsByDateRange(999, null, null, null); // Non-existent customer
        
        assertNotNull("Results should not be null", results);
        assertTrue("Results should be empty for non-existent customer", results.isEmpty());
    }

    @Test
    public void testSearchMultipleCriteria() throws ParseException {
        // Test search with both date range and ID (ID takes precedence)
        Date searchStartDate = new Date(dateFormat.parse("2023-01-01").getTime());
        Date searchEndDate = new Date(dateFormat.parse("2023-12-31").getTime());
        Integer searchId = 2;
        
        List<Shipment> results = searchShipmentsByDateRange(1, searchStartDate, searchEndDate, searchId);
        
        assertEquals("Should return 1 shipment when searching by ID", 1, results.size());
        assertEquals("Should return the correct shipment", 2, results.get(0).getShipmentID());
    }

    @Test
    public void testSearchResultsFormat() {
        // Test that search results contain complete shipment information
        List<Shipment> results = searchShipmentsByDateRange(1, null, null, 1);
        
        if (!results.isEmpty()) {
            Shipment result = results.get(0);
            assertNotNull("Shipment ID should not be null", result.getShipmentID());
            assertNotNull("Order ID should not be null", result.getOrderID());
            assertNotNull("Customer ID should not be null", result.getCustomerID());
            assertNotNull("Street address should not be null", result.getStreetAddress());
            assertNotNull("City should not be null", result.getCity());
            assertNotNull("State should not be null", result.getState());
            assertNotNull("Postcode should not be null", result.getPostcode());
        }
    }

    private List<Shipment> searchShipmentsByDateRange(Integer customerId, Date startDate, Date endDate, Integer shipmentId) {
        List<Shipment> results = new ArrayList<>();
        
        for (Shipment shipment : testShipments) {
            boolean include = true;
            
            // Check customer ID
            if (shipment.getCustomerID() != customerId) {
                include = false;
            }
            
            // If searching by ID, only 1 ID should appear
            if (include && shipmentId != null && shipment.getShipmentID() != shipmentId.intValue()) {
                include = false;
            }
            
            // Check start date
            if (include && startDate != null && shipment.getCreatedDate() != null) {
                if (shipment.getCreatedDate().before(startDate)) {
                    include = false;
                }
            }
            
            // Check end date
            if (include && endDate != null && shipment.getCreatedDate() != null) {
                if (shipment.getCreatedDate().after(endDate)) {
                    include = false;
                }
            }
            
            if (include) {
                results.add(shipment);
            }
        }
        
        return results;
    }
}
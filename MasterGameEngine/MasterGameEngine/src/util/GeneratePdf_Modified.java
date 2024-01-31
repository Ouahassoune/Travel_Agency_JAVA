package util;

import com.codingerror.model.AddressDetails;
import com.codingerror.model.HeaderDetails;
import com.codingerror.model.Product;
import com.codingerror.model.ProductTableHeader;
import com.codingerror.service.CodingErrorPdfInvoiceCreator;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneratePdf_Modified{
    private CodingErrorPdfInvoiceCreator cepdf;
    // Constructor to accept information from the validation page
    public static String filepath;

    public GeneratePdf_Modified(String Nom, List<Product> productList) throws FileNotFoundException {
        LocalDate ld = LocalDate.now();
        String pdfName = ld + ".pdf";
//        String filep;
        filepath = "C:\\Users\\dell\\Desktop\\testpdf\\MasterGameEngine\\"+pdfName;
        cepdf = new CodingErrorPdfInvoiceCreator(pdfName);
        cepdf.createDocument();

        // Create Header
        HeaderDetails header = new HeaderDetails();
        header.setInvoiceNo(generateRandomInvoiceNo()).setInvoiceDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).build();
        cepdf.createHeader(header);
        //Header End

        // Create Address
        //Create Address start
        AddressDetails addressDetails=new AddressDetails();


        addressDetails
                .setBillingCompany("OGY")
                .setBillingName("Agence De Voyage")
                .setBillingAddress("Ecole Nationale des Sciences Appliquées \nd'Al Hoceima BP 03,\n Ajdir Al-Hoceima")
                .setBillingEmail("OGY@gmail.com")
                .setShippingName(Nom)
                .build();

        cepdf.createAddress(addressDetails);
        //Address end


        // Create Product Table Header
        ProductTableHeader productTableHeader = new ProductTableHeader();
        cepdf.createTableHeader(productTableHeader);

        // Modify and Create Product List

        productList = cepdf.modifyProductList(productList);
        cepdf.createProduct(productList);

        // Term and Condition
        List<String> TncList = new ArrayList<>();
        TncList.add("1. The Seller shall not be liable to the Buyer directly or indirectly for any loss or damage suffered by the Buyer.");
        TncList.add("2. The Seller warrants the product for one (1) year from the date of shipment");
        String imagePath = "MasterGameEngine/src/icons/Street App Location Itinerary Logo .png";
        cepdf.createTnc(TncList, false, imagePath);

        // Term and condition end
        System.out.println("pdf generated");




    }
    // Method to generate a random invoice number
    private String generateRandomInvoiceNo() {
        return "INV" + new Random().nextInt(10000);
    }



}
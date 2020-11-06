import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Iterator;
import java.util.Objects;

public class Inventory{

    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();



    public void addPart(Part newPart){
        allParts.add(newPart);
    }

    public void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    public Part lookupPart(int partId){
        Part foundPart = null;
        for (int i = 0; i < allParts.size(); i++){
            if (allParts.get(i).getId() == partId){
                foundPart = allParts.get(i);
            }
        }
        return foundPart;
    }

    public Product lookupProduct(int productId){

        Product foundProduct = null;

        for (int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).getId() == productId){
                foundProduct = allProducts.get(i);
            }
        }
        return foundProduct;
    }

    public Part lookupPart(String partName){

        Part foundPart = null;

        for (int i = 0; i < allParts.size(); i++){

            if (Objects.equals(allParts.get(i).getName(), partName)){

                foundPart = allParts.get(i);

            }

        }
        return foundPart;

    }

    public Product lookupProduct(String productName){

        Product foundProduct = null;

        for (int i = 0; i < allProducts.size(); i++){

            if (Objects.equals(allProducts.get(i).getName(), productName)){

                foundProduct = allProducts.get(i);

            }

        }
        return foundProduct;

    }

    public void updatePart(int index, Part updatedPart){

        getAllParts().set(index, updatedPart);

    }

    public void updateProduct(int index, Product updatedProduct){

        getAllProducts().set(index, updatedProduct);

    }

    public boolean deletePart(Part selectedPart){

        allParts.remove(selectedPart);

        if (allParts.contains(selectedPart)){
            return false;
        }

        return true;

    }

    public boolean deleteProduct(Product selectedProduct){

        allProducts.remove(selectedProduct);

        if (allProducts.contains(selectedProduct)){
            return false;
        }

        return true;

    }

    public ObservableList<Part> getAllParts(){

        return this.allParts;

    }

    public ObservableList<Product> getAllProducts(){

        return this.allProducts;

    }

}

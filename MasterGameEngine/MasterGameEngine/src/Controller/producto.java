package Controller;

import com.codingerror.model.Product;

import java.util.Objects;
import java.util.Optional;

public class producto {


        private Optional<String> pname;
        private int quantity;
        private double priceperpeice;

        public producto(String pname, int quantity, double priceperpeice) {
//            super();
            this.pname = Optional.ofNullable(pname);
            this.quantity = quantity;
            this.priceperpeice = priceperpeice;
        }




}

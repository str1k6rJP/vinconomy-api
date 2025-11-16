package com.skully.vinconomy.model.builder;

import com.skully.vinconomy.model.ShopProduct;
import com.skully.vinconomy.model.ShopProductId;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShopProductBuilder {

    private static final Logger log = LoggerFactory.getLogger(ShopProductBuilder.class);

    private ShopProductId id;
    private String productName;
    private String productCode;
    private int productQuantity;
    private String productAttributes;

    private String currencyName;
    private String currencyCode;
    private int currencyQuantity;
    private String currencyAttributes;

    private int totalStock;

    public ShopProduct build() {
        verifyIdState();
        verifyCurrencyState();
        verifyProductState();
        var result = new ShopProduct();
        result.setId(id);
        result.setProductName(productName);
        result.setProductCode(productCode);
        result.setProductQuantity(productQuantity);
        result.setProductAttributes(productAttributes);
        result.setCurrencyName(currencyName);
        result.setCurrencyCode(currencyCode);
        result.setCurrencyQuantity(currencyQuantity);
        result.setCurrencyAttributes(currencyAttributes);
        result.setTotalStock(totalStock);
        return result;
    }

    private void verifyIdState() {
        if (id == null) throw new IllegalStateException("id is null");
    }

    private void verifyProductState() {
        if (productCode == null) throw new IllegalStateException("product is absent");
    }

    private void verifyCurrencyState() {
        if (currencyCode == null) throw new IllegalStateException("currency is absent");
    }

    public ShopProductBuilder withId(ShopProductId id) {
        this.id = id;
        return this;
    }

    public ShopProductBuilder withProduct(String productCode) {
        return withProduct(null, productCode, null);
    }

    public ShopProductBuilder withProduct(String productCode, String productName) {
        return withProduct(productCode, productName, null);
    }

    public ShopProductBuilder withProduct(String productCode, int productQuantity) {
        return withProduct(productCode, null, productQuantity);
    }

    public ShopProductBuilder withCurrency(String currencyCode) {
        return withCurrency(currencyCode, null, null);
    }

    public ShopProductBuilder withCurrency(String currencyCode, String currencyName) {
        return withCurrency(currencyCode, currencyName, null);
    }

    public ShopProductBuilder withCurrency(String currencyCode, int currencyQuantity) {
        return withCurrency(currencyCode, null, currencyQuantity);
    }

    public ShopProductBuilder withProduct(String productCode, @Nullable String productName, @Nullable Integer productQuantity) {
        verifyIdState();
        this.productCode = productCode;
        this.productName = productName;
        this.productQuantity = productQuantity == null ? 0 : productQuantity;
        return this;
    }

    public ShopProductBuilder withProductAttributes(String productAttributes) {
        verifyIdState();
        verifyProductState();
        this.productAttributes = productAttributes;
        return this;
    }

    public ShopProductBuilder withCurrency(String currencyCode, @Nullable String currencyName, @Nullable Integer currencyQuantity) {
        verifyIdState();
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.currencyQuantity = currencyQuantity == null ? 0 : currencyQuantity;
        return this;
    }

    public ShopProductBuilder withCurrencyAttributes(String currencyAttributes) {
        verifyIdState();
        verifyCurrencyState();
        this.currencyAttributes = currencyAttributes;
        return this;
    }

    public ShopProductBuilder withTotalStock(int totalStock) {
        verifyIdState();
        verifyCurrencyState();
        try {
            verifyProductState();
            this.totalStock = totalStock;
        } catch (IllegalStateException e) {
            log.warn("No product specified for transaction, stock fallback to 0");
            this.totalStock = 0;
        }
        return this;
    }

}

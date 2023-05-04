package com.codunite.model;

public class DashShoppingModel {
   private String str_proId,str_proName,str_proSlug,
           str_proImg,str_price,str_spl_status,str_spl_price,str_section;
   private int drawble;

   public DashShoppingModel(String str_section, String str_proId, String str_proName, String str_proSlug, String str_proImg, String str_price, String str_spl_status, String str_spl_price) {
   }


   public int getDrawble() {
      return drawble;
   }

   public void setDrawble(int drawble) {
      this.drawble = drawble;
   }

   public DashShoppingModel(int drawble) {
      this.drawble = drawble;
   }

   public DashShoppingModel(String str_proId, String str_proName, String str_proSlug, String str_proImg, String str_price,
                            String str_spl_status, String str_spl_price, String str_section, int drawble) {
      this.str_proId = str_proId;
      this.str_proName = str_proName;
      this.str_proSlug = str_proSlug;
      this.str_proImg = str_proImg;
      this.str_price = str_price;
      this.str_spl_status = str_spl_status;
      this.str_spl_price = str_spl_price;
      this.str_section = str_section;
      this.drawble = drawble;
   }

   public String getStr_proId() {
      return str_proId;
   }

   public void setStr_proId(String str_proId) {
      this.str_proId = str_proId;
   }

   public String getStr_proName() {
      return str_proName;
   }

   public void setStr_proName(String str_proName) {
      this.str_proName = str_proName;
   }

   public String getStr_proSlug() {
      return str_proSlug;
   }

   public void setStr_proSlug(String str_proSlug) {
      this.str_proSlug = str_proSlug;
   }

   public String getStr_proImg() {
      return str_proImg;
   }

   public void setStr_proImg(String str_proImg) {
      this.str_proImg = str_proImg;
   }

   public String getStr_price() {
      return str_price;
   }

   public void setStr_price(String str_price) {
      this.str_price = str_price;
   }

   public String getStr_spl_status() {
      return str_spl_status;
   }

   public void setStr_spl_status(String str_spl_status) {
      this.str_spl_status = str_spl_status;
   }

   public String getStr_spl_price() {
      return str_spl_price;
   }

   public void setStr_spl_price(String str_spl_price) {
      this.str_spl_price = str_spl_price;
   }

   public String getStr_section() {
      return str_section;
   }

   public void setStr_section(String str_section) {
      this.str_section = str_section;
   }
}

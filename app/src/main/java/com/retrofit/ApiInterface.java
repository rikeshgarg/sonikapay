package com.retrofit;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    String[] strUrlName = {"", "", ""};
    String[] strUrlText = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

    String UPDATEFCM = "userDetail";
    String LOGIN = "userAuth"; //ok
    String LOGNUSER = "loginAuth"; //ok
    String SIGNUP = "registerAuth"; //unused
    String OPERATORLIST = "operatorList"; //ok
    String CIRCLELIST = "circleList"; //ok
    String FORGOTPWDAUTH = "forgotAuth";
    String FORGOTPWDOTPVERIFY = "forgotOTPAuth";
    String FORGOTUPDATEPWD = "updatePasswordAuth";
    String CHANGEPASSWORD = "changePassword";
    String CHANGETRANCATIONPASSWORD = "changeTransactionPassword";
    String GETPACKAGELIST = "getPackage";//unused
    String GETDIRECTDOWNLINE = "getUserDirectDownline";
    String GETTOTALDOWNLINE = "getUserTotalDownline";
    String RECHARGEOTPAUTH = "rechargeOTPAuth";//unused
    String GETDIRECTACTIVEDOWNLINE = "getUserDirectActiveDownline";
    String GETDIRECTDEACTIVEDOWNLINE = "getUserDirectDeactiveDownline";
    String GETTOTALACTIVEDOWNLINE = "getUserTotalActiveDownline";
    String GETTOTALDEACTIVEDOWNLINE = "getUserTotalDeactiveDownline";
    String GETDIRECTINCOME = "getDirectIncome";
    String GETLEVELINCOME = "getLevelIncome";
    String GETCASHBACKINCOME = "getCashbackIncome";

    String GETMATCHINGBINARY = "getMatchingBinaryIncome";//unused
    String WALLETHISTORY = "getMainWalletList";
    String EWALLETHISTORY = "getEWalletList";
    String REQUESTHISTORY = "getRequestHistory";
    String RECHARGEHISTORY = "getRechargeHistory";
    String RECHARGECOMMISIONHISTORY = "getRechargeCommisionHistory";
    String GETELECTRICITYFIELD = "getElectricityOperatorForm";//unused
    String GETELECTRICITYBILLERDETAIL = "getElectricityBillerDetail";//unused
    String ADDWALLET = "addWallet";//unused
    String WALLETTRANSFER = "walletAuth";//unused
    String WALLETOTPAUTH = "walletsTransferOtpAuth";//unused
    String MTRANSFERHISTORY = "getFundTransferList";
    String MOBILETRANSFER = "fundTransferAutho";//unused
    String MOBILETRANSFERVERIFY = "transferOTPAuth";//unused
    String UPGRADE = "upgrade";//unused
    String GETDASHBOARD = "dashboardAuth";//unused

    String GETCOUNTRYLIST = "getCountryList";//unused
    String GetSTATELIST = "getStateList";
    String GetCITYLIST = "getCityList";
    String GETMEMBERLIST = "getMemberList";//unused
    String RECHARGEAUTH = "rechargeAuth";//unused
    String ELECTRICRECHARGEAUTH = "electricityAuth";
    String UPADATEUSERDATA = "updateuserdata";
    String SUBMITCOMPALINT = "ticketAuth";
    String GETTICKETLIST = "getTicketList";
    String GETLOAN = "getLoan";
    String GETLOANINSTALLMENT = "getLoanInstallment";
    String EMIPAYAUTH = "emiPayAuth";
    String ADDMEMBERAUTH = "addMemberAuth";
    String GETCREDITCARDHISTORY = "getCreditCardHistory";
    String SENDSMS = "";
    String GETCCTRANSFERHISTORY = "getCreditTransferHistory";
    String USERKYC = "kycAuth";
    String USERKYCDETAIL = "getUserKycDetails";
    String USERKYC_CHANGE_ACCOUNT = "changeAccountAuth";

    String GETTICKETTYPELIST = "getTicketTypeList";
    String REQUESTAMOUNT = "requestwalletamount";

    String GET_CONTACT_CONTENT = "getContactContent";
    String SUBMIT_CONTACT_CONTENT = "submitContactUsForm";
    String GET_PRIVACY_CONTENT = "getPrivacyContent";
    String GET_WEBSITE_DISCLAIMER = "getWebsiteDisclaimer";
    String GET_PRODUCT_DESIGN = "getProductDesign";
    String GET_TERMS_AND_CONDITIONS = "getTermsAndConditions";

    String ROFFERS = "getROfferList";
    String RDTHOFFERS = "getDTHROfferList";
    String VIEWALLPLANS = "getPlanList";
    String VIEWALLDTHPLANS = "getDTHPlanList";
    String GETOPERATORID = "getOperatorId";
    String GETRECHARGECOMMISIONLIST = "getRechargeCommission";
    String GETBBPSCOMMISIONLIST = "getBBPSCommisionList";
    String GETBBPSLIVECOMMISIONLIST = "getBbpsCommission";
    String GETRECHARGEINCOME = "getRechargeIncome";
    String GETBBPSINCOME = "getBbpsIncome";

    String GETMONEYTRANSFERCOMMISIONLIST = "getMoneyTransferCommisionList";
    String GETAEPSCOMMISIONLIST = "getAEPSCommisionList";
    String GETBBSPHISTORY = "getBBSPHistory";
    String GETBBSPLIVEHISTORY = "getBBPSLiveHistory";

    String GETBBSPELECTOPERATOR = "getBbpsElectricityOperator";
    String GETBBSPELECTFORM = "getBbpsElectricityFormParams";//?biller_id={biller_id}
    String GETBBSPELECBILLFETCH = "electricityBillFetchAuth";
    String GETBBSPBILLPAY = "electricityBillPayAuth";
    String GETBBSPFASTTAGOPERATOR = "getBbpsFastagOperator";
    String GETBBSPFASTAGFORM = "getBbpsFastagFormParams";//?biller_id={biller_id}
    String GETBBSPFASTTAGBILLFETCH = "fastagBillFetchAuth";
    String GETBBSPFASTTAGBILLPAY = "fastagBillPayAuth";
    String RAISERECHGCOMPALINT = "complainAuth";
    String RAISEBBPSCOMPALINT = "bbpsComplainAuth";
    String GETCOMPLAINTLIST = "getComplainHistory";
    String GETMEMBERBYMOBILE = "getMemberByMobile";
    String GETBBSPSERVICELIST = "getBbpsServiceList";
    String GETBBSPSERVICEOPERATOR = "getServiceOperator";
    String GETBBSPSERVICEFORM = "getServiceFormParams";
    String GETBBSPSERVICEBILLFETCH = "serviceBillFetchAuth";
    String GETBBSPSERVICEBILLPAY = "serviceBillPayAuth";
    String REGISTERTRANSFER = "moneyTransferRegister";
    String MONEYTRANSFERREGISTEROTP = "moneyTransferOtpRegister";
    String PAYOUTAUTH = "payoutAuth";
    String GETPAYOUTTRANSFERREPORT = "fundTransferHistory";
    String MONEYTRANSFERlOGIN = "moneyTransferLogin";
    String MONEYTRANSFERAUTH = "fundTransferAuth";
    String MONEYTRANSFEROTPAUTH = "fundTransferOtpAuth";
    String GETMONEYTRANSFERHISTORY = "getMoneyTransferHistory";
    String GETBBPSOPERATORLIST = "getBbpsOperatorList";
    String GETBBPSCIRCLELIST = "getBbpsCircleList";

    // Add Fund Apis
    String GET_FUNDREQUEST_MANUUAL_QR = "getManualQr";
    String FUNDREQUESTAUTH = "fundRequestAuth";
    String GETFUNDREQUESTLIST = "getFundRequestList";

    // Common AEPS Apis
    String AEPSBANKLIST = "getAepsBankList";
    String AEPSSTATELIST = "getAepsStateList";
    String AEPSCITYLIST = "getAepsCityList";

    // Main AEPS Apis
    String AEPSACTIVEAUTH = "aepsActiveAuth";
    String AEPSOTOAUTH = "aepsOtpAuth";
    String AEPSRESENDOTP = "aepsResendOtpAuth";
    String AEPSUPLOADKYC = "aepsKycBioAuth";
    String AEPSHISTORY = "getAepsHistory";
    String AEPSAPIAUTH = "aepsApiAuth";

    // ICIC AEPS Apis
    String AEPSICICACTIVEAUTH = "iciciAepsActiveAuth";
    String AEPSICICOTOAUTH = "iciciAepsOtpAuth";
    String AEPSICICRESENDOTP = "iciciAepsResendOtpAuth";
    String AEPSICICUPLOADKYC = "iciciAepsKycBioAuth";
    String AEPSICICHISTORY = "getIciciAepsHistory";
    String AEPSICICAPIAUTH = "iciciAepsApiAuth";

    // New AEPS Service Apis
    String NEW_AEPS_STATUS_ACTIVE = "newAepsStatusActive";
    String NEW_AEPSACTIVEAUTH = "newAepsActiveAuth";
    String NEW_AEPSAPIAUTH = "newAepsApiAuth";
    String NEW_AEPSHISTORY = "getNewAepsHistory";

    String EWALLETPAYOUTAUTH = "ewalletBankTransferAuth";
    String EWALLETPAYOUTREPORT = "getEwalletBankTransferHistory";
    String EWALLETSETTLEMENT = "ewalletSettlementAuth";
    String GETLOANREQUEST = "getLoanRequest";

    String GENERATETOKENAUTH = "genratePinAuth";
    String PINLIST = "pinList";
    String PINREQUESTHISTORY = "pinRequestList";
    String REQUESTTOKENAUTH = "requestPinAuth";

    String GETTENURELIST = "getTenureList";
    String APPLYLOANAUTH = "applyLoanAuth";
    String GETLOANCHARGES = "getLoanCharges";
    String USERACTIVATEUPI = "activateUpi";

    String GETUPITRANSCATIONS = "getUpiTransactionHistory";
    String GETUPIID = "getQr";
    String UPITOPUPAUTH = "upiTopupAuth";
    String STATICQRAUTH = "staticQrAuth";

    String UPI_ACTIVEQRAUTH = "activeCyrusQrAuth";
    String UPI_ACTIVEQROTPAUTH = "activeCyrusQrOtpAuth";
    String UPI_STATICQRAUTH = "staticCyrusQrAuth";

    String ADDTRANSFERBENEFICERY = "addBenificary";
    String GETBENEFICIARYLIST = "benificaryList";
    String BENEFICIARYOTPAUTH = "beneficiaryOtpAuth";
    String GETAEPSWALLETHISTORY = "getAepsWalletList";
    String GETCOMMISIONWALLETHISTORY = "getCommissionWalletList";
    String GETPOINTWALLETHISTORY = "getPointWalletList";
    String QRCODe = "getWalletQr";

    String MAIN_WALLET_PAYOUT_AUTH = "mainWalletPayoutAuth";
    String MAIN_WALLET_PAYOUT_OTP_AUTH = "mainWalletPayoutOtpAuth";

    String AEPSWALLETPAYOUTAUTH = "aepsWalletPayoutAuth";
    String AEPSWALLETPAYOUTOTPAUTH = "aepsWalletPayoutOtpAuth";
    String ADDAEPSPAYOUTBENIFICARY = "addAepsPayoutBenificary";
    String GETAEPSPAYOUTBENIFICARYLIST = "aepsPayoutBenificaryList";

    String COMMISSION_WALLET_PAYOUT_AUTH = "commissionWalletPayoutAuth";
    String COMMISSION_WALLET_PAYOUT_OTP_AUTH = "commissionWalletPayoutOtpAuth";

    String GET_DOCUMENT = "getDocument";
    String GET_DOCUMENT_CATEGORY = "documentCategory";

    String GETUPLINE = "userUplineDetail";
    String GETMAINWALLETTRANSFER = "mainWalletTransferAuth";
    String GETAEPSWALLETTRANSFER = "aepsWalletTransferAuth";
    String GETCOMMISSIONWALLET = "commissionWalletTransferAuth";
    String GETSCANCODE = "scanMainWalletTransferAuth";
    String PRODUCTLIST = "getProductList";
    String SEARCHPRODUCT = "searchProduct";
    String PRODUCTDETAIL = "getProductDetail";
    String CATEGORYID = "getCategoryList";
    String PRODUCTSECTIONLIST = "productSectionList";
    String GETSUBCATEGORY = "getSubCategoryList";
    String GETCONTACT = "getContactSupport";
    String AEPSINCOME = "getAepsIncome";
    String AEPSCOMMISSION = "getAepsCommission";
    String MAINWALLETPAYOUTREPORT = "mainWalletfundTransferHistory";
    String AEPSWALLETPAYOUTREPORT = "aepsWalletfundTransferHistory";
    String COMMISSIONWALLETPAYOUTREPORT = "commissionWalletfundTransferHistory";
    String UTICOUPONLIST = "getUtiCouponList";
    String PURCHASECOUPONAUTH = "purchaseCouponAuth";
    String PANCARDACTIVE = "pancardActiveAuth";
    String PANCARDKYCDATA = "getPancardKycData";

    String GET_AFFILIATE_LIST = "affiliateList"; //unused
    String ADD_NOMINEE_AUTH = "addNomineeAuth";
    String GET_NOMINEE_DATA = "getUserNomineeDetails";
    String GETZOOMDETAILS = "zoom_detail";
    String GET_NOTIFICATION_LIST = "notificationList";

    String GET_VENDOR_DETAILS = "getVendorRequestDetails";
    String VENDOR_REGISTER_AUTH = "vendorRegisterAuth";
    String GENERATE_VENDOR_BILL_AUTH = "generateVendorBillAuth";
    String VENDOR_BILLER_LIST = "vendorBillList";
    String SEARCH_STORE_AUTH = "searchStoreAuth";
    String SEARCH_DOWNLINE = "searchDownlineUser";
    String GETVENDOR_PACKAGELIST = "getVendorPackageList";
    String VENDOR_PACKAGE_PURCHASEAUTH = "vendorPackagePurchaseAuth";
    String UPLOAD_VENDOR_BANNER = "uploadVendorBanner";
    String DELETE_VENDOR_BANNER_AUTH = "deleteVendorBannerAuth";
    String GET_VENDOR_BANNERLIST = "getVendorBannerList";
    String GET_AREA_BANNERLIST = "getAreaBannerList"; //unused

    // New AEPS Payout Apis
    String NEWAEPSBANKLIST = "getNewAepsPayoutBankList";
    String NEW_PAYOUT_BENE_LIST = "newPayoutBeneList";
    String NEWAEPS_BENEAUTH = "newPayoutBeneAuth";
    String NEW_PAYOUT_DOC_AUTH = "newPayoutDocumentAuth";
    String AEPS_PAYOUT_CHECK_STATUSAUTH = "aepsPayoutCheckStatusAuth";
    String NEW_AEPS_PAYOUT_LIST = "newPayoutList";
    String NEW_AEPS_PAYOUT_AUTH = "newPayoutAuth";
    String DELETE_BENEFICIARY = "deleteBeneficiary";
    String GATEWAYHISTORY = "getPgHistory";
    String GATEWAYSTATUS = "checkPgOrderStatus";
    String GETOTTPLANS = "getOttPlan";
    String GetFranchiseStateList = "getFranchiseStateList";
    String GetFranchiseDistrictList = "getFranchiseDistrictList";
    String GetFranchiseBlockList = "getFranchiseBlockList";
    String VERIFYSIGNUPOTP = "registerOtpVerifyAuth";
    String GETUSERNAME = "getUserName";
    String GETDEMOLINK = "getDemoLink";

    String WITHDRAWALREQUESTAMOUNT = "getWithdrawalRequest";

    @FormUrlEncoded
    @POST(WITHDRAWALREQUESTAMOUNT)
    Call<String> WithdrawalRequest(@Field("userID") String userid,
                             @Field("amount") String amount,
                             @Field("transaction_password") String transaction_password);
    // 218 Apis

    @FormUrlEncoded
    @POST(GETDEMOLINK)
    Call<String> GetDemoLink(@Field("service_id") String service_id);

    @FormUrlEncoded
    @POST(VERIFYSIGNUPOTP)
    Call<String> VerifySignupOtp(@Field("otp_code") String otp_code,
                                 @Field("encrypt_otp_code") String encrypt_otp_code);

    @FormUrlEncoded
    @POST(GetFranchiseStateList)
    Call<String> getFrancStateList(@Field("operator_id") String operator_id);

    @FormUrlEncoded
    @POST(GetFranchiseDistrictList)
    Call<String> getDistrictList(@Field("state_id") String operator_id);

    @FormUrlEncoded
    @POST(GetFranchiseBlockList)
    Call<String> getBlockList(@Field("district_id") String operator_id);

    @FormUrlEncoded
    @POST(GATEWAYHISTORY)
    Call<String> getPGHistory(@Field("user_id") String userId,
                              @Field("fromDate") String fromDate,
                              @Field("toDate") String toDate,
                              @Field("page_no") String page_no);

    @FormUrlEncoded
    @POST(GETOTTPLANS)
    Call<String> getOttPlans(@Field("operator_id") String operator_id);

    @FormUrlEncoded
    @POST(GATEWAYSTATUS)
    Call<String> getPGStatus(@Field("user_id") String userId,
                             @Field("order_id") String order_id);


    @FormUrlEncoded
    @POST(DELETE_BENEFICIARY)
    Call<String> deleteBenificary(@Field("userID") String user_id,
                                  @Field("bene_id") String bene_id);

    @GET(NEWAEPSBANKLIST)
    Call<String> getNewAepsBankList();

    @FormUrlEncoded
    @POST(NEW_PAYOUT_BENE_LIST)
    Call<String> newPayoutBeneList(@Field("userID") String userID);

    @FormUrlEncoded
    @POST(NEWAEPS_BENEAUTH)
    Call<String> newPayoutBeneAuth(@Field("userID") String userID,
                                   @Field("account_holder_name") String name,
                                   @Field("bank") String bank,
                                   @Field("account_number") String account_number,
                                   @Field("ifsc") String ifsc);

    @FormUrlEncoded
    @POST(NEW_PAYOUT_DOC_AUTH)
    Call<String> newPayoutDocumentAuth(@Field("userID") String userID,
                                       @Field("bene_id") String bene_id,
                                       @Field("document_type") String document_type,
                                       @Field("passbook") String passbook,
                                       @Field("panimage") String panimage,
                                       @Field("aadhar_front") String aadhar_front,
                                       @Field("aadhar_back") String aadhar_back);

    @Multipart
    @POST(NEW_PAYOUT_DOC_AUTH)
    Call<String> newPayoutDocumentAuthPart(@Part("userID") RequestBody userID,
                                           @Part("bene_id") RequestBody bene_id,
                                           @Part("document_type") RequestBody document_type,
                                           @Part MultipartBody.Part passbook,
                                           @Part("panimage") RequestBody panimage,
                                           @Part("aadhar_front") RequestBody aadhar_front,
                                           @Part("aadhar_back") RequestBody aadhar_back);

    @FormUrlEncoded
    @POST(AEPS_PAYOUT_CHECK_STATUSAUTH)
    Call<String> aepsPayoutCheckStatusAuth(@Field("userID") String userID,
                                           @Field("ref_id") String ref_id);

    @FormUrlEncoded
    @POST(NEW_AEPS_PAYOUT_LIST)
    Call<String> newPayoutList(@Field("userID") String userID);

    @FormUrlEncoded
    @POST(NEW_AEPS_PAYOUT_AUTH)
    Call<String> newAepsPayoutAuth(@Field("userID") String user_id,
                                   @Field("bene_id") String mobile,
                                   @Field("txn_pass") String txn_pass,
                                   @Field("amount") String amount);

    @FormUrlEncoded
    @POST(UTICOUPONLIST)
    Call<String> getUTICoupon(@Field("userID") String userid);

    @FormUrlEncoded
    @POST(PURCHASECOUPONAUTH)
    Call<String> getPurchaseCoupon(@Field("userID") String user_id,
                                   @Field("psa_login_id") String login_id,
                                   @Field("coupon") String coupon);

    @FormUrlEncoded
    @POST(PANCARDACTIVE)
    Call<String> getPancardactive(@Field("userID") String user_id,
                                  @Field("name") String mobile,
                                  @Field("email") String accholdername,
                                  @Field("mobile") String accno,
                                  @Field("aadhar_card") String confirmaccno,
                                  @Field("pancard") String ifsc);

    @FormUrlEncoded
    @POST(PANCARDKYCDATA)
    Call<String> getPancardKyc(@Field("userID") String userid);

    @GET(GET_AFFILIATE_LIST)
    Call<String> getAffiliateList();

    @GET(CATEGORYID)
    Call<String> GetcategoryId();

    @GET(PRODUCTSECTIONLIST)
    Call<String> Getproductlist();

    @FormUrlEncoded
    @POST(SEARCHPRODUCT)
    Call<String> GetSearch(@Field("keyword") String prdkey);

    @FormUrlEncoded
    @POST(GETCONTACT)
    Call<String> Getsupport(@Field("userID") String userid);

    @FormUrlEncoded
    @POST(AEPSINCOME)
    Call<String> Getaepsincome(@Field("userID") String userid);

    @FormUrlEncoded
    @POST(AEPSCOMMISSION)
    Call<String> GetaepsCommission(@Field("userID") String userid);


    @FormUrlEncoded
    @POST(GETSCANCODE)
    Call<String> getscancode(@Field("userID") String user_id,
                             @Field("encoded_member_id") String mem_id,
                             @Field("amount") String amount);

    @FormUrlEncoded
    @POST(GETMAINWALLETTRANSFER)
    Call<String> mainwallettransfer(@Field("userID") String user_id,
                                    @Field("member_id") String mem_id,
                                    @Field("amount") String amount,
                                    @Field("transaction_password") String txnType);

    @FormUrlEncoded
    @POST(GETAEPSWALLETTRANSFER)
    Call<String> aepswallettransfer(@Field("userID") String user_id,

                                    @Field("amount") String amount,

                                    @Field("transaction_password") String txnType,
                                    @Field("description") String description);

    @FormUrlEncoded
    @POST(GETCOMMISSIONWALLET)
    Call<String> commissionwalettransfer(@Field("userID") String user_id,
                                         @Field("amount") String amount,
                                         @Field("transaction_password") String txnType,
                                         @Field("description") String description);

    @FormUrlEncoded
    @POST(GET_DOCUMENT)
    Call<String> getDocument(@Field("cat_id") String cat_id);

    @POST(GET_DOCUMENT_CATEGORY)
    Call<String> getDocumentCategory();

    @FormUrlEncoded
    @POST(GETUPLINE)
    Call<String> getupline(@Field("user_id") String userid);

    @FormUrlEncoded
    @POST(MAIN_WALLET_PAYOUT_AUTH)
    Call<String> mainpayoutAuth(@Field("userID") String user_id,
                                @Field("mobile") String mobile,
                                @Field("account_holder_name") String accholdername,
                                @Field("account_no") String accno,
                                @Field("confirm_account_no") String confirmaccno,
                                @Field("ifsc") String ifsc,
                                @Field("txn_pass") String txnType,
                                @Field("amount") String amount);

    @FormUrlEncoded
    @POST(MAIN_WALLET_PAYOUT_OTP_AUTH)
    Call<String> mainWalletPayoutOtpAuth(@Field("userID") String user_id,
                                         @Field("otp_code") String otp_code);

    @FormUrlEncoded
    @POST(AEPSWALLETPAYOUTAUTH)
    Call<String> aepspayoutAuth(@Field("userID") String user_id,
                                @Field("bene_id") String bene_id,
                                @Field("txn_pass") String txn_pass,
                                @Field("amount") String amount);

    @FormUrlEncoded
    @POST(AEPSWALLETPAYOUTOTPAUTH)
    Call<String> aepsWalletPayoutOtpAuth(@Field("userID") String user_id,
                                         @Field("otp_code") String otp_code);

    @FormUrlEncoded
    @POST(GETAEPSPAYOUTBENIFICARYLIST)
    Call<String> aepsPayoutBenificaryList(@Field("userID") String user_id);

    @FormUrlEncoded
    @POST(ADDAEPSPAYOUTBENIFICARY)
    Call<String> addAepsPayoutBenificary(@Field("userID") String user_id,
                                         @Field("account_holder_name") String accholdername,
                                         @Field("bank_name") String bnkname,
                                         @Field("account_number") String accno,
                                         @Field("ifsc") String ifsc);

    @FormUrlEncoded
    @POST(COMMISSION_WALLET_PAYOUT_AUTH)
    Call<String> commissionpayoutAuth(@Field("userID") String user_id,
                                      @Field("mobile") String mobile,
                                      @Field("account_holder_name") String accholdername,
                                      @Field("account_no") String accno,
                                      @Field("confirm_account_no") String confirmaccno,
                                      @Field("ifsc") String ifsc,
                                      @Field("txn_pass") String txnType,
                                      @Field("amount") String amount);

    @FormUrlEncoded
    @POST(COMMISSION_WALLET_PAYOUT_OTP_AUTH)
    Call<String> commissionWalletPayoutOtpAuth(@Field("userID") String user_id,
                                               @Field("otp_code") String otp_code);

    @FormUrlEncoded
    @POST(PAYOUTAUTH)
    Call<String> payoutAuth(@Field("userID") String user_id,
                            @Field("mobile") String mobile,
                            @Field("account_holder_name") String accholdername,
                            @Field("account_no") String accno,
                            @Field("confirm_account_no") String confirmaccno,
                            @Field("ifsc") String ifsc,
                            @Field("amount") String amount,
                            @Field("bankID") String bankID,
                            @Field("txnType") String txnType);


    @FormUrlEncoded
    @POST(QRCODe)
    Call<String> getwalletqr(@Field("userID") String userid);

    @FormUrlEncoded
    @POST(UPITOPUPAUTH)
    Call<String> UPITopup(@Field("userID") String user_id,
                          @Field("txnid") String txdid);

    @FormUrlEncoded
    @POST(STATICQRAUTH)
    Call<String> staticQrAuth(@Field("userID") String user_id);

    @FormUrlEncoded
    @POST(UPI_ACTIVEQRAUTH)
    Call<String> activeCyrusQrAuth(@Field("userID") String userID,
                                   @Field("name") String name,
                                   @Field("email") String email,
                                   @Field("mobile") String mobile,
                                   @Field("pancard_no") String pancard_no,
                                   @Field("aadhar_no") String aadhar_no,
                                   @Field("zip_code") String zip_code,
                                   @Field("address") String address);

    @FormUrlEncoded
    @POST(UPI_ACTIVEQROTPAUTH)
    Call<String> activeCyrusQrOtpAuth(@Field("userID") String userID,
                                      @Field("otp_code") String otp_code,
                                      @Field("response_id") String response_id,
                                      @Field("activation_id") String activation_id);

    @FormUrlEncoded
    @POST(UPI_STATICQRAUTH)
    Call<String> staticCyrusQrAuth(@Field("userID") String userID);


    @FormUrlEncoded
    @POST(GETCASHBACKINCOME)
    Call<String> getCASHBACKIncome(@Field("userID") String userid);
    @FormUrlEncoded
    @POST(GETLEVELINCOME)
    Call<String> getLevelIncome(@Field("userID") String userid);

    @FormUrlEncoded
    @POST(GETUPITRANSCATIONS)
    Call<String> GetUpiTranscation(@Field("userID") String userid);

    @FormUrlEncoded
    @POST(GETUPIID)
    Call<String> GetUpiId(@Field("userID") String userid);

    @FormUrlEncoded
    @POST(USERACTIVATEUPI)
    Call<String> ActivateUpi(@Field("user_id") String userId,
                             @Field("name") String password,
                             @Field("mobile") String cpassword);

    @FormUrlEncoded
    @POST(PINREQUESTHISTORY)
    Call<String> GetPinRequestHistory(@Field("userID") String userId);


    @FormUrlEncoded
    @POST(EWALLETSETTLEMENT)
    Call<String> EWalletSettlement(@Field("userID") String userId,
                                   @Field("amount") String from,
                                   @Field("description") String to);

    @FormUrlEncoded
    @POST(GENERATETOKENAUTH)
    Call<String> generateTokenAuth(@Field("userID") String userid,
                                   @Field("package_id") String pckageId,
                                   @Field("token_number") String token);

    @FormUrlEncoded
    @POST(PINLIST)
    Call<String> GetPinList(@Field("userID") String userId);

    @FormUrlEncoded
    @POST(GETLOANREQUEST)
    Call<String> getLoanRequest(@Field("userID") String user_id,
                                @Field("fromDate") String from_date,
                                @Field("toDate") String to_date);


    @GET(GET_FUNDREQUEST_MANUUAL_QR)
    Call<String> getFundRequestManualQr();

    @FormUrlEncoded
    @POST(FUNDREQUESTAUTH)
    Call<String> fundRequestAuth(@Field("userID") String userId,
                                 @Field("amount") String token,
                                 @Field("txnID") String txnID,
                                 @Field("payment_screenshot") String pay_screenshot);

    @FormUrlEncoded
    @POST(GETFUNDREQUESTLIST)
    Call<String> getFundRequestList(@Field("userID") String userId,
                                    @Field("fromDate") String fromDate,
                                    @Field("toDate") String toDate,
                                    @Field("page_no") String page_no);


    @FormUrlEncoded
    @POST(AEPSAPIAUTH)
    Call<JsonObject> AddAepsRequest(@Field("user_data") String user_data);

    @POST(AEPSBANKLIST)
    Call<JsonObject> getAllbankNamedetails();

    @POST(AEPSBANKLIST)
    Call<String> getAllbankName();

    @GET(AEPSSTATELIST)
    Call<String> getAepsState();

    @FormUrlEncoded
    @POST(AEPSCITYLIST)
    Call<String> getAepsCity(@Field("state_id") String state_id);

    @FormUrlEncoded
    @POST(AEPSACTIVEAUTH)
    Call<String> getAepsActiveAuth(@Field("user_id") String user_id,
                                   @Field("first_name") String first_name,
                                   @Field("last_name") String last_name,
                                   @Field("mobile") String mobile,
                                   @Field("shop_name") String shop_name,
                                   @Field("state_id") String state_id,
                                   @Field("city_id") String city_id,
                                   @Field("address") String address,
                                   @Field("pin_code") String pin_code,
                                   @Field("aadhar_no") String aadhar_no,
                                   @Field("pancard_no") String pancard_no,
                                   @Field("aadhar_photo") String aadhar_photo,
                                   @Field("pancard_photo") String pancard_photo);

    //    @FormUrlEncoded
//    @POST(AEPSHISTORY)
//    Call<String> GetAepsHistory(@Field("userID") String userId,
//                                @Field("fromDate") String from,
//                                @Field("toDate") String to,
//                                @Field("keyword") String keyword);
//
    @FormUrlEncoded
    @POST(AEPSOTOAUTH)
    Call<String> getAepOtp(@Field("user_id") String user_id,
                           @Field("encodeFPTxnId") String encodeFPTxnId,
                           @Field("otp_code") String otp_code);

    @FormUrlEncoded
    @POST(AEPSRESENDOTP)
    Call<String> getAepsResendOtp(@Field("user_id") String user_id,
                                  @Field("encodeFPTxnId") String encodeFPTxnId);

    @FormUrlEncoded
    @POST(AEPSUPLOADKYC)
    Call<String> getAepsUpload(@Field("user_id") String user_id,
                               @Field("encodeFPTxnId") String encodeFPTxnId,
                               @Field("BiometricData") String otp_code);


    /* ICIC Aeps Apis */
    @FormUrlEncoded
    @POST(AEPSICICACTIVEAUTH)
    Call<String> getIcicAepsActiveAuth(@Field("user_id") String user_id,
                                       @Field("first_name") String first_name,
                                       @Field("last_name") String last_name,
                                       @Field("mobile") String mobile,
                                       @Field("shop_name") String shop_name,
                                       @Field("state_id") String state_id,
                                       @Field("city_id") String city_id,
                                       @Field("address") String address,
                                       @Field("pin_code") String pin_code,
                                       @Field("aadhar_no") String aadhar_no,
                                       @Field("pancard_no") String pancard_no,
                                       @Field("aadhar_photo") String aadhar_photo,
                                       @Field("pancard_photo") String pancard_photo);

    @FormUrlEncoded
    @POST(AEPSICICOTOAUTH)
    Call<String> getIcicAepOtp(@Field("user_id") String user_id,
                               @Field("encodeFPTxnId") String encodeFPTxnId,
                               @Field("otp_code") String otp_code);

    @FormUrlEncoded
    @POST(AEPSICICRESENDOTP)
    Call<String> getIcicAepsResendOtp(@Field("user_id") String user_id,
                                      @Field("encodeFPTxnId") String encodeFPTxnId);

    @FormUrlEncoded
    @POST(AEPSICICUPLOADKYC)
    Call<String> getIcicAepsUpload(@Field("user_id") String user_id,
                                   @Field("encodeFPTxnId") String encodeFPTxnId,
                                   @Field("BiometricData") String otp_code);


    @FormUrlEncoded
    @POST(AEPSICICHISTORY)
    Call<String> GetIcicAepsHistory(@Field("userID") String userId,
                                    @Field("fromDate") String from,
                                    @Field("toDate") String to,
                                    @Field("page_no") String page);

    @FormUrlEncoded
    @POST(AEPSICICAPIAUTH)
    Call<JsonObject> AddIcicAepsRequest(@Field("user_data") String user_data);
    /* ICIC Aeps Apis */


    /* New Aeps Apis */
    @FormUrlEncoded
    @POST(NEW_AEPS_STATUS_ACTIVE)
    Call<String> newAepsStatusActive(@Field("userID") String user_id);

    @FormUrlEncoded
    @POST(NEW_AEPSACTIVEAUTH)
    Call<String> newAepsActiveAuth(@Field("user_id") String user_id,
                                   @Field("first_name") String first_name,
                                   @Field("last_name") String last_name,
                                   @Field("mobile") String mobile,
                                   @Field("shop_name") String shop_name,
                                   @Field("state_id") String state_id,
                                   @Field("city_id") String city_id,
                                   @Field("address") String address,
                                   @Field("pin_code") String pin_code,
                                   @Field("aadhar_no") String aadhar_no,
                                   @Field("pancard_no") String pancard_no,
                                   @Field("aadhar_photo") String aadhar_photo,
                                   @Field("pancard_photo") String pancard_photo);

    @FormUrlEncoded
    @POST(NEW_AEPSAPIAUTH)
    Call<JsonObject> newAepsApiAuth(@Field("user_data") String user_data);

    @FormUrlEncoded
    @POST(NEW_AEPSHISTORY)
    Call<String> getNewAepsHistory(@Field("userID") String userId,
                                   @Field("fromDate") String from,
                                   @Field("toDate") String to);

    /* New Aeps Apis */


    @GET(GETBBPSOPERATORLIST)
    Call<String> getBbpsOperator();

    @GET(GETBBPSCIRCLELIST)
    Call<String> getBbpsCircle();

    @FormUrlEncoded
    @POST(GETMONEYTRANSFERHISTORY)
    Call<String> getMoneyTransferHistory(@Field("userID") String user_id);

    @FormUrlEncoded
    @POST(MONEYTRANSFERAUTH)
    Call<String> moneyTransferAuth(@Field("userID") String user_id,
                                   @Field("bene_id") String ben_id,
                                   @Field("amount") String amount,
                                   @Field("txn_pass") String txn_pass);

    @FormUrlEncoded
    @POST(MONEYTRANSFEROTPAUTH)
    Call<String> fundTransferOtpAuth(@Field("userID") String user_id,
                                     @Field("otp_code") String otp_code);

    @FormUrlEncoded
    @POST(ADDTRANSFERBENEFICERY)
    Call<String> addBeneficiary(@Field("userID") String user_id,
                                @Field("account_holder_name") String accholdername,
                                @Field("bank_name") String bnkname,
                                @Field("account_number") String accno,
                                @Field("ifsc") String ifsc);

    @FormUrlEncoded
    @POST(BENEFICIARYOTPAUTH)
    Call<String> beneficiaryOtpAuth(@Field("userID") String user_id,
                                    @Field("beneId") String ben_id,
                                    @Field("mobile") String mobile,
                                    @Field("otp_code") String otp_code);

    @FormUrlEncoded
    @POST(GETBENEFICIARYLIST)
    Call<String> getBeneficiaryList(@Field("userID") String user_id);

    @FormUrlEncoded
    @POST(MONEYTRANSFERlOGIN)
    Call<String> moneyTransferLogin(@Field("userID") String user_id,
                                    @Field("mobile") String mobile);


    @FormUrlEncoded
    @POST(GETPAYOUTTRANSFERREPORT)
    Call<String> getPayoutTransferHistory(@Field("user_id") String user_id,
                                          @Field("fromDate") String from,
                                          @Field("toDate") String to);


    @FormUrlEncoded
    @POST(MAINWALLETPAYOUTREPORT)
    Call<String> getMainPayout(@Field("user_id") String user_id,
                               @Field("fromDate") String from,
                               @Field("toDate") String to,
                               @Field("page_no") String page_no);

    @FormUrlEncoded
    @POST(COMMISSIONWALLETPAYOUTREPORT)
    Call<String> getCommsionPayout(@Field("user_id") String user_id,
                                   @Field("fromDate") String from,
                                   @Field("toDate") String to,
                                   @Field("page_no") String page);

    @FormUrlEncoded
    @POST(AEPSWALLETPAYOUTREPORT)
    Call<String> getAepsPayout(@Field("user_id") String user_id,
                               @Field("fromDate") String from,
                               @Field("toDate") String to,
                               @Field("page_no") String page_no);


    @FormUrlEncoded
    @POST(EWALLETPAYOUTREPORT)
    Call<String> getEWalletPayoutHistory(@Field("user_id") String user_id,
                                         @Field("fromDate") String from,
                                         @Field("toDate") String to);


    @FormUrlEncoded
    @POST(EWALLETPAYOUTAUTH)
    Call<String> EWalletpayoutAuth(@Field("userID") String user_id,
                                   @Field("mobile") String mobile,
                                   @Field("account_holder_name") String accholdername,
                                   @Field("account_no") String accno,
                                   @Field("confirm_account_no") String confirmaccno,
                                   @Field("ifsc") String ifsc,
                                   @Field("amount") String amount,
                                   @Field("bankID") String bankID,
                                   @Field("txnType") String txnType);


    @FormUrlEncoded
    @POST(MONEYTRANSFERREGISTEROTP)
    Call<String> moneyTransferOtpRegister(@Field("userID") String user_id,
                                          @Field("token") String token,
                                          @Field("otp_code") String otp);


    @GET(GETBBSPSERVICELIST)
    Call<String> getBbpsServiceList();

    @GET(GETBBSPSERVICEOPERATOR)
    Call<String> GetServiceOperatorList(@Query("service_id") String user_id);

    @GET(GETBBSPSERVICEFORM)
    Call<String> GetBBPSFORm(@Query("service_id") String user_id,
                             @Query("biller_id") String biller_id);


    @FormUrlEncoded
    @POST(REGISTERTRANSFER)
    Call<String> moneyTransferRegister(@Field("userID") String user_id,
                                       @Field("first_name") String fname,
                                       @Field("last_name") String lname,
                                       @Field("mobile") String mobile,
                                       @Field("dob") String dob,
                                       @Field("address") String address,
                                       @Field("pin_code") String pincode);


    @FormUrlEncoded
    @POST(GETBBSPSERVICEBILLFETCH)
    Call<String> getServicePayBillHistory(@Field("user_id") String user_id,
                                          @Field("service_id") String service_id,
                                          @Field("biller_id") String biller_id,
                                          @Field("para1") String para1,
                                          @Field("para2") String para2,
                                          @Field("para3") String para3,
                                          @Field("para4") String para4,
                                          @Field("para5") String para5,
                                          @Field("para6") String para6,
                                          @Field("para7") String para7,
                                          @Field("para8") String para8);


    @FormUrlEncoded
    @POST(GETBBSPSERVICEBILLPAY)
    Call<String> ServiceBillPayAuth(@Field("user_id") String user_id,
                                    @Field("service_id") String service_id,
                                    @Field("biller_id") String biller_id,
                                    @Field("para1") String para1,
                                    @Field("para2") String para2,
                                    @Field("para3") String para3,
                                    @Field("para4") String para4,
                                    @Field("para5") String para5,
                                    @Field("para6") String para6,
                                    @Field("para7") String para7,
                                    @Field("para8") String para8,
                                    @Field("amount") String amount,
                                    @Field("txn_pass") String txn_pass);

    @FormUrlEncoded
    @POST(GETBBSPSERVICEBILLPAY)
    Call<String> ServiceBillPayAuthOTT(@Field("user_id") String user_id,
                                       @Field("service_id") String service_id,
                                       @Field("biller_id") String biller_id,
                                       @Field("para1") String para1,
                                       @Field("para2") String para2,
                                       @Field("para3") String para3,
                                       @Field("para4") String para4,
                                       @Field("para5") String para5,
                                       @Field("para6") String para6,
                                       @Field("para7") String para7,
                                       @Field("para8") String para8,
                                       @Field("amount") String amount,
                                       @Field("planrefid") String planrefid,
                                       @Field("planid") String planid,
                                       @Field("txn_pass") String txn_pass);

    @FormUrlEncoded
    @POST(GETCOMPLAINTLIST)
    Call<String> GetComplaintList(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(GETMEMBERBYMOBILE)
    Call<String> GetMemberByMobile(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST(RAISERECHGCOMPALINT)
    Call<String> RaiseComplaintAUth(@Field("userID") String user_id,
                                    @Field("recharge_id") String biller_id,
                                    @Field("description") String para1);

    @FormUrlEncoded
    @POST(RAISEBBPSCOMPALINT)
    Call<String> RaiseBBPPSComplaintAUth(@Field("userID") String user_id,
                                         @Field("recharge_id") String biller_id,
                                         @Field("description") String para1);

    @GET(GETBBSPFASTTAGOPERATOR)
    Call<String> getBbpsFAstagOperator();

    @FormUrlEncoded
    @POST(GETBBSPFASTAGFORM)
    Call<String> FAstagBillFetchAuth(
            @Field("biller_id") String billerId);

    @FormUrlEncoded
    @POST(GETBBSPFASTTAGBILLFETCH)
    Call<String> getFAstagPayBillHistory(@Field("user_id") String user_id,
                                         @Field("biller_id") String biller_id,
                                         @Field("para1") String para1,
                                         @Field("para2") String para2,
                                         @Field("para3") String para3);

    @FormUrlEncoded
    @POST(GETBBSPFASTTAGBILLPAY)
    Call<String> FAstagBillPayAuth(@Field("user_id") String user_id,
                                   @Field("biller_id") String biller_id,
                                   @Field("para1") String para1,
                                   @Field("para2") String para2,
                                   @Field("para3") String para3,
                                   @Field("amount") String amount,
                                   @Field("txn_pass") String txn_pass);

    @GET(GETBBSPELECTOPERATOR)
    Call<String> getBbpsElectricityOperator();

    @FormUrlEncoded
    @POST(GETBBSPELECTFORM)
    Call<String> electricityBillFetchAuth(
            @Field("biller_id") String billerId);

    @FormUrlEncoded
    @POST(GETBBSPELECBILLFETCH)
    Call<String> getPayBillHistory(@Field("user_id") String user_id,
                                   @Field("biller_id") String biller_id,
                                   @Field("para1") String para1,
                                   @Field("para2") String para2,
                                   @Field("para3") String para3);

    @FormUrlEncoded
    @POST(GETBBSPBILLPAY)
    Call<String> electricityBillPayAuth(@Field("user_id") String user_id,
                                        @Field("biller_id") String biller_id,
                                        @Field("para1") String para1,
                                        @Field("para2") String para2,
                                        @Field("para3") String para3,
                                        @Field("amount") String amount,
                                        @Field("txn_pass") String txn_pass);

    @FormUrlEncoded
    @POST(GETBBSPHISTORY)
    Call<String> getPayBillHistorys(@Field("userID") String code,
                                    @Field("fromDate") String from,
                                    @Field("toDate") String to,
                                    @Field("page_no") String page);

    @FormUrlEncoded
    @POST(GETBBSPLIVEHISTORY)
    Call<String> getBBPSLiveHistory(@Field("userID") String code,
                                    @Field("fromDate") String from,
                                    @Field("toDate") String to,
                                    @Field("page_no") String page);

    @FormUrlEncoded
    @POST(GETRECHARGECOMMISIONLIST)
    Call<String> getRechargeCommisionList(@Field("userID") String code);

    @FormUrlEncoded
    @POST(GETRECHARGEINCOME)
    Call<String> getRechargeIncome(@Field("userID") String code);

    @FormUrlEncoded
    @POST(GETBBPSINCOME)
    Call<String> getBbpsIncome(@Field("userID") String code);

    @FormUrlEncoded
    @POST(GETBBPSCOMMISIONLIST)
    Call<String> getBBPSCommisionList(@Field("userID") String code);

    @FormUrlEncoded
    @POST(GETBBPSLIVECOMMISIONLIST)
    Call<String> getBBPSLiveCommisionList(@Field("userID") String code);

    @FormUrlEncoded
    @POST(GETMONEYTRANSFERCOMMISIONLIST)
    Call<String> getMoneyTransferCommisionList(@Field("user_id") String code);

    @FormUrlEncoded
    @POST(GETAEPSCOMMISIONLIST)
    Call<String> getAEPSCommisionList(@Field("user_id") String code);

    @GET(GET_CONTACT_CONTENT)
    Call<String> getContactContent();

    @FormUrlEncoded
    @POST(SUBMIT_CONTACT_CONTENT)
    Call<String> submitContactUsForm(@Field("name") String name,
                                     @Field("email") String email,
                                     @Field("mobile") String mobile,
                                     @Field("message") String message);

    @GET(GET_PRIVACY_CONTENT)
    Call<String> getPrivacyContent();

    @GET(GET_WEBSITE_DISCLAIMER)
    Call<String> getStringSecond();

    @GET(GET_PRODUCT_DESIGN)
    Call<String> getStringThiord();

    @GET(GET_TERMS_AND_CONDITIONS)
    Call<String> getStringForth();

    @GET(GETCOUNTRYLIST)
    Call<String> GETCOUNTRYLIST();

    @GET(GETTICKETTYPELIST)
    Call<String> GetTicketTypelist();

    @FormUrlEncoded
    @POST(GETOPERATORID)
    Call<String> GetOperatorList(@Field("mobile") String mob);

    @FormUrlEncoded
    @POST(GetSTATELIST)
    Call<String> GetSTATELIST(@Field("countryCode") String code);

    @GET(GetCITYLIST)
    Call<String> getCityList();

    @FormUrlEncoded
    @POST(GETMEMBERLIST)
    Call<String> GETMEMBERLIST(@Field("user_id") String code);

    @FormUrlEncoded
    @POST(GETTICKETLIST)
    Call<String> GetTicketKList(@Field("user_id") String code);

    @FormUrlEncoded
    @POST(OPERATORLIST)
    Call<String> GetOperator(@Field("type") String operatorType);

    @GET(CIRCLELIST)
    Call<String> CIRCLELIST();

    @FormUrlEncoded
    @POST(RECHARGEAUTH)
    Call<String> RECHARGEAUTH(@Field("userID") String userid,
                              @Field("number") String mob,
                              @Field("operator") String coun,
                              @Field("circle") String state,
                              @Field("amount") String city,
                              @Field("type") String type,
                              @Field("rechargeType") String rechargeType,
                              @Field("txn_pass") String txn_pass);



    @FormUrlEncoded
    @POST(GETUSERNAME)
    Call<String> GetUsername(@Field("user_code") String userId);

    String GETUPGRADEINCOME = "GETUPGRADEINCOME";

    @FormUrlEncoded
    @POST(GETUPGRADEINCOME)
    Call<String> GetUpgradeIncome(@Field("userID") String userId);

    @FormUrlEncoded
    @POST(REQUESTAMOUNT)
    Call<String> RequestWalletBalance(@Field("user_id") String userid,
                                      @Field("amount") String amount,
                                      @Field("transid") String transid);

    @FormUrlEncoded
    @POST(ELECTRICRECHARGEAUTH)
    Call<String> ElectricRechargeAuth(@Field("user_id") String userid,
                                      @Field("number") String mob,
                                      @Field("operator") String oprator,
                                      @Field("customer_name") String custname,
                                      @Field("reference_id") String refId,
                                      @Field("amount") String city);

    @FormUrlEncoded
    @POST(ADDMEMBERAUTH)
    Call<String> ADDMEMBERAUTH(@Field("user_id") String userid,
                               @Field("role_id") String roleid,
                               @Field("name") String name,
                               @Field("email") String email,
                               @Field("mobile") String mob,
                               @Field("password") String pass,
                               @Field("transaction_password") String transpas,
                               @Field("country_id") String coun,
                               @Field("state_id") String state,
                               @Field("city") String city);

    @FormUrlEncoded
    @POST(UPDATEFCM)
    Call<String> UpdateFcm(@Field("user_id") String userid,
                           @Field("fcm_id") String fcmid,
                           @Field("device_id") String device_id);


    @FormUrlEncoded
    @POST(LOGNUSER)
    Call<String> LoginAttempt(@Field("transaction_password") String username,
                              @Field("encode_login_id") String password);

    @FormUrlEncoded
    @POST(LOGIN)
    Call<String> Login(@Field("username") String username,
                       @Field("password") String password,
                       @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST(SIGNUP)
    Call<String> Register(@Field("name") String name,
                          @Field("mobile") String phone,
                          @Field("email") String email,
                          @Field("refercode") String refercode,
                          @Field("password") String password,
                          @Field("transaction_password") String tpassword,
                          @Field("pan_card") String pancard);

    @GET(CIRCLELIST)
    Call<String> GetCircleList();

    @FormUrlEncoded
    @POST(FORGOTPWDAUTH)
    Call<String> AuthForgotPwd(@Field("mobile") String operatorType);

    @FormUrlEncoded
    @POST(FORGOTPWDOTPVERIFY)
    Call<String> ForgotOtpverify(@Field("otp") String operatorType);

    @FormUrlEncoded
    @POST(FORGOTUPDATEPWD)
    Call<String> ForgotUpdatePwd(@Field("userID") String userId,
                                 @Field("password") String password,
                                 @Field("cpassword") String cpassword);

    @FormUrlEncoded
    @POST(UPADATEUSERDATA)
    Call<String> UpdateUserData(@Field("userID") String userId,
                                @Field("name") String password,
                                @Field("address") String address,
                                @Field("pincode") String pincode,
                                @Field("city_id") String city_id,
                                @Field("state_id") String state_id,
                                @Field("block_id") String block_id,
                                @Field("photo") String photo);

    @FormUrlEncoded
    @POST(SUBMITCOMPALINT)
    Call<String> SubMitComplain(@Field("user_id") String title,
                                @Field("subject") String desc,
                                @Field("type_id") String type,
                                @Field("message") String custId,
                                @Field("photo") String photo);

    @Multipart
    @POST(UPADATEUSERDATA)
    Call<String> UpdateUserData(@Part("userID") RequestBody userId,
                                @Part("name") RequestBody password,
                                @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST(CHANGEPASSWORD)
    Call<String> ChangePassword(@Field("userID") String userId,
                                @Field("opw") String oldpassword,
                                @Field("npw") String newpassword,
                                @Field("cpw") String confirmpassword);

    @FormUrlEncoded
    @POST(CHANGETRANCATIONPASSWORD)
    Call<String> ChangeTranscationPassword(@Field("userID") String userId,
                                           @Field("otpw") String oldpassword,
                                           @Field("ntpw") String newpassword,
                                           @Field("ctpw") String confirmpassword);

    @FormUrlEncoded
    @POST(RECHARGEOTPAUTH)
    Call<String> RechargeOtpConfirm(@Field("userID") String userId,
                                    @Field("otp") String otp);

    @FormUrlEncoded
    @POST(WALLETHISTORY)
    Call<String> GetWalletHistory(@Field("user_id") String userId,
                                  @Field("fromDate") String from,
                                  @Field("toDate") String to,
                                  @Field("page_no") String page_no);

    @FormUrlEncoded
    @POST(EWALLETHISTORY)
    Call<String> GetEWalletHistory(@Field("user_id") String userId,
                                   @Field("fromDate") String from,
                                   @Field("toDate") String to);

    @FormUrlEncoded
    @POST(GETAEPSWALLETHISTORY)
    Call<String> GetAepsWalletHistory(@Field("user_id") String userId,
                                      @Field("fromDate") String from,
                                      @Field("toDate") String to,
                                      @Field("page_no") String page_no);

    @FormUrlEncoded
    @POST(GETCOMMISIONWALLETHISTORY)
    Call<String> GetCommissionWalletHistory(@Field("user_id") String userId,
                                            @Field("fromDate") String from,
                                            @Field("toDate") String to,
                                            @Field("page_no") String page_no);

    @FormUrlEncoded
    @POST(GETPOINTWALLETHISTORY)
    Call<String> GetPointWalletHistory(@Field("user_id") String userId,
                                       @Field("fromDate") String from,
                                       @Field("toDate") String to,
                                       @Field("page_no") String page_no);

    @FormUrlEncoded
    @POST(REQUESTHISTORY)
    Call<String> GetRequestHistory(@Field("user_id") String userId,
                                   @Field("fromDate") String from,
                                   @Field("toDate") String to);

    @FormUrlEncoded
    @POST(ADDWALLET)
    Call<String> AddWallet(@Field("userID") String operatorType,
                           @Field("amount") String amount);

    @FormUrlEncoded
    @POST(WALLETTRANSFER)
    Call<String> WalletTransfer(@Field("user_id") String userid,
                                @Field("member_id") String memberid,
                                @Field("type") String type,
                                @Field("amount") String amount,
                                @Field("description") String desc);

    @FormUrlEncoded
    @POST(WALLETOTPAUTH)
    Call<String> WalletTransferOtpAuth(@Field("userID") String userid,
                                       @Field("otp_code") String otpcode);

    @FormUrlEncoded
    @POST(MTRANSFERHISTORY)
    Call<String> MtransferHistory(@Field("user_id") String userid);

    @FormUrlEncoded
    @POST(MOBILETRANSFER)
    Call<String> TransferMwalletAmount(@Field("userID") String userid,
                                       @Field("bene_id") String mobilenumber,
                                       @Field("txn_pass") String holderName,
                                       @Field("amount") String accountNumber);

    @FormUrlEncoded
    @POST(MOBILETRANSFERVERIFY)
    Call<String> VerifyMwalletAmount(@Field("userID") String userid,
                                     @Field("otp_code") String holderName,
                                     @Field("encode_transaction_id") String transId);

    @FormUrlEncoded
    @POST(UPGRADE)
    Call<String> UpgradeAccount(@Field("userID") String userid,
                                @Field("package_id") String pkgId,
                                @Field("token") String pin,
                                @Field("memberID") String membeId,
                                @Field("upgrade_by") String upgrade_by);


    @GET(GETPACKAGELIST)
    Call<String> GetPackageList();

    @FormUrlEncoded
    @POST(GETDIRECTDOWNLINE)
    Call<String> GetDirectDownLine(@Field("userID") String userId);

    @FormUrlEncoded
    @POST(GETTOTALDOWNLINE)
    Call<String> GetTotalDownLine(@Field("userID") String userId);

    @FormUrlEncoded
    @POST(GETTOTALACTIVEDOWNLINE)
    Call<String> GetTotalActiveDownLine(@Field("userID") String userId);

    @FormUrlEncoded
    @POST(GETTOTALDEACTIVEDOWNLINE)
    Call<String> GetTotalDeactiveDownLine(@Field("userID") String userId);

    @FormUrlEncoded
    @POST(GETDIRECTACTIVEDOWNLINE)
    Call<String> GetDirectActiveDownLine(@Field("userID") String userId);

    @FormUrlEncoded
    @POST(GETDIRECTDEACTIVEDOWNLINE)
    Call<String> GetDirectDeActiveDownLine(@Field("userID") String userId);

    @FormUrlEncoded
    @POST(GETMATCHINGBINARY)
    Call<String> GetMatchingBinary(@Field("userID") String userId);

    @FormUrlEncoded
    @POST(GETDIRECTINCOME)
    Call<String> GetDirectIncome(@Field("userID") String userId);

    @GET(GETDASHBOARD)
    Call<String> GetDashBoard();

    @FormUrlEncoded
    @POST(GETSUBCATEGORY)
    Call<String> GetSUBCATEGORY(@Field("catID") String userId);

    @FormUrlEncoded
    @POST(PRODUCTLIST)
    Call<String> GetProductList(@Field("catID") String userId);

    @FormUrlEncoded
    @POST(PRODUCTDETAIL)
    Call<String> GetProductDetail(@Field("proID") String prdtId);

    @FormUrlEncoded
    @POST(RECHARGEHISTORY)
    Call<String> GetRechargeHistory(@Field("userID") String userId,
                                    @Field("fromDate") String from,
                                    @Field("toDate") String to,
                                    @Field("page_no") String page);

    @FormUrlEncoded
    @POST(RECHARGECOMMISIONHISTORY)
    Call<String> GetRechargeCommisionHistory(@Field("userID") String userId,
                                             @Field("fromDate") String from,
                                             @Field("toDate") String to);

    @FormUrlEncoded
    @POST(GETELECTRICITYFIELD)
    Call<String> GetElectricField(@Field("code") String code);

    @FormUrlEncoded
    @POST(GETELECTRICITYBILLERDETAIL)
    Call<String> GetElectricBillerDetail(@Field("userID") String userid,
                                         @Field("account_number") String accountnumber,
                                         @Field("code") String operatorcode);

    @FormUrlEncoded
    @POST(GETCREDITCARDHISTORY)
    Call<String> GetCreditCardHistory(@Field("userID") String code);

    @FormUrlEncoded
    @POST(GETLOAN)
    Call<String> GetLoan(@Field("userID") String code,
                         @Field("loan_id") String loanid);

    @FormUrlEncoded
    @POST(GETLOANINSTALLMENT)
    Call<String> GetLoanInstallment(@Field("userID") String code,
                                    @Field("loan_display_id") String loanid);

    @FormUrlEncoded
    @POST(EMIPAYAUTH)
    Call<String> PayEmi(@Field("userID") String code,
                        @Field("loan_display_id") String loanid,
                        @Field("emi_id") String emi_id);

    @FormUrlEncoded
    @POST(ROFFERS)
    Call<String> GetROffers(@Field("mobile") String code,
                            @Field("operator") String emi_id);

    @FormUrlEncoded
    @POST(RDTHOFFERS)
    Call<String> GetRDthOffers(@Field("mobile") String code,
                               @Field("operator") String emi_id);

    @FormUrlEncoded
    @POST(VIEWALLPLANS)
    Call<String> GetAllPlans(@Field("circle") String code,
                             @Field("operator") String emi_id);

    @FormUrlEncoded
    @POST(VIEWALLDTHPLANS)
    Call<String> GetAllDthPlans(@Field("number") String card_num,
                                @Field("operator") String emi_id);


    @FormUrlEncoded
    @POST(APPLYLOANAUTH)
    Call<String> applyLoanAuth(@Field("userID") String user_id,
                               @Field("name") String name,
                               @Field("mobile") String mobile,
                               @Field("email") String email,
                               @Field("pancard_no") String pancard_no,
                               @Field("loan_amount") String loan_amount,
                               @Field("tenure") String tenure);

    @FormUrlEncoded
    @POST(GETLOANCHARGES)
    Call<String> getLoanCharges(@Field("tenureID") String tenure_id,
                                @Field("loan_amount") String loan_amnt);

    @GET(GETTENURELIST)
    Call<String> getTenureList();

//    @FormUrlEncoded
//    @POST(AEPSHISTORY)
//    Call<String> getAepsHistory(@Field("userID") String userID,
//                                @Field("fromDate") String fromDate,
//                                @Field("toDate") String toDate);

    @FormUrlEncoded
    @POST(AEPSHISTORY)
    Call<String> GetAepsHistory(@Field("userID") String userId,
                                @Field("fromDate") String from,
                                @Field("toDate") String to,
                                @Field("page_no") String page);

    @FormUrlEncoded
    @POST(REQUESTTOKENAUTH)
    Call<String> requestTokenAuth(@Field("userID") String userid,
                                  @Field("package_id") String pckageId,
                                  @Field("token_number") String token,
                                  @Field("payment_screenshot") String screenshot);

    @FormUrlEncoded
    @POST(SIGNUP)
    Call<String> Register(@Field("name") String name,
                          @Field("mobile") String phone,
                          @Field("email") String email,
                          @Field("password") String password,
                          @Field("transaction_password") String tpassword,
                          @Field("member_position") String mposition,
                          @Field("referral_id") String refid,
                          @Field("pan_card") String pancard);

    @FormUrlEncoded
    @POST(GETCCTRANSFERHISTORY)
    Call<String> CCTransferHistory(@Field("userID") String userid);

    @FormUrlEncoded
    @POST(USERKYC)
    Call<String> UserKYC(@Field("userID") String userid,
                         @Field("accountName") String acc_name,
                         @Field("accountNumber") String acc_num,
                         @Field("mobile_no") String mobile_no,
                         @Field("ifsc") String ifsc,
                         @Field("bankName") String bankname,
                         @Field("aadhar_no") String aadhar_no,
                         @Field("dob") String dob,
                         @Field("adharFront") String aadharFront,
                         @Field("adharBack") String aadharBack,
                         @Field("pancard") String photo);

    @FormUrlEncoded
    @POST(USERKYCDETAIL)
    Call<String> USERKYCDETAIL(@Field("userID") String userid);

    @FormUrlEncoded
    @POST(USERKYC_CHANGE_ACCOUNT)
    Call<String> changeAccountAuth(@Field("userID") String userID,
                                   @Field("account_name") String account_name,
                                   @Field("account_number") String account_number,
                                   @Field("ifsc") String ifsc,
                                   @Field("bank_name") String bank_name);

    @FormUrlEncoded
    @POST(ADD_NOMINEE_AUTH)
    Call<String> addNomineeAuth(@Field("userID") String userid,
                                @Field("nominee_name") String name,
                                @Field("mobile") String mobile,
                                @Field("email") String email,
                                @Field("address") String address);

    @FormUrlEncoded
    @POST(GET_NOMINEE_DATA)
    Call<String> getNomineeData(@Field("userID") String userid);

    String GET_AVAILABLE_PIN = "getAvailablePin";
    String TRANSFER_PIN_AUTH = "transferPinAuth";
    String GET_RANK_LIST = "getRankList";
    String GET_RANKWISE_TEAMLIST = "getRankWiseTeamList";
    String GET_LEVEL_LIST = "getLevelList";
    String GET_LEVELWISE_TEAMLIST = "getLevelWiseTeamList";

    @FormUrlEncoded
    @POST(TRANSFER_PIN_AUTH)
    Call<String> transferPinAuth(@Field("userID") String userID,
                                 @Field("package_id") String package_id,
                                 @Field("token_number") String token_number,
                                 @Field("transfer_to_user") String transfer_to_user);

    @FormUrlEncoded
    @POST(GET_AVAILABLE_PIN)
    Call<String> getAvailablePin(@Field("userID") String userID,
                                 @Field("package_id") String package_id);

    @FormUrlEncoded
    @POST(GET_RANK_LIST)
    Call<String> getRankList(@Field("userID") String userID);

    @FormUrlEncoded
    @POST(GET_RANKWISE_TEAMLIST)
    Call<String> getRankWiseTeamList(@Field("userID") String userID,
                                     @Field("rank_id") String rank_id,
                                     @Field("page_no") String page_no);

    @FormUrlEncoded
    @POST(GET_LEVEL_LIST)
    Call<String> getLevelList(@Field("userID") String userID);

    @FormUrlEncoded
    @POST(GET_LEVELWISE_TEAMLIST)
    Call<String> getLevelWiseTeamList(@Field("userID") String userID,
                                      @Field("level_id") String level_id,
                                      @Field("page_no") String page_no);

    @GET(GETZOOMDETAILS)
    Call<String> getZoomDetails();

    @FormUrlEncoded
    @POST(GET_NOTIFICATION_LIST)
    Call<String> notificationList(@Field("userID") String userID);


    @FormUrlEncoded
    @POST(GET_VENDOR_DETAILS)
    Call<String> getVendorRequestDetails(@Field("userID") String userID);

    @FormUrlEncoded
    @POST(VENDOR_REGISTER_AUTH)
    Call<String> vendorRegisterAuth(@Field("userID") String userID,
                                    @Field("business_name") String business_name,
                                    @Field("mobile") String mobile,
                                    @Field("alternate_mobile") String alternate_mobile,
                                    @Field("gst_no") String gst_no,
                                    @Field("commission") String commission,
                                    @Field("address") String address,
                                    @Field("description") String description,
                                    @Field("pincode") String pincode,
                                    @Field("city_id") String city_id,
                                    @Field("state_id") String state_id,
                                    @Field("latitude") String latitude,
                                    @Field("longitude") String longitude,
                                    @Field("profile") String profile);

    @FormUrlEncoded
    @POST(GENERATE_VENDOR_BILL_AUTH)
    Call<String> generateVendorBillAuth(@Field("userID") String userID,
                                        @Field("member_id") String member_id,
                                        @Field("amount") String amount);

    @FormUrlEncoded
    @POST(VENDOR_BILLER_LIST)
    Call<String> vendorBillList(@Field("userID") String userID);

    @FormUrlEncoded
    @POST(SEARCH_STORE_AUTH)
    Call<String> searchStoreAuth(@Field("userID") String userID,
                                 @Field("state_id") String state_id,
                                 @Field("city_id") String city_id,
                                 @Field("pincode") String pincode);

    @FormUrlEncoded
    @POST(SEARCH_DOWNLINE)
    Call<String> searchDownline(@Field("userID") String userID,
                                 @Field("user_code") String pincode);


    @FormUrlEncoded
    @POST(GETVENDOR_PACKAGELIST)
    Call<String> getVendorPackageList(@Field("userID") String userID);

    @FormUrlEncoded
    @POST(VENDOR_PACKAGE_PURCHASEAUTH)
    Call<String> vendorPackagePurchaseAuth(@Field("userID") String userID,
                                           @Field("package_id") String package_id);

    @FormUrlEncoded
    @POST(UPLOAD_VENDOR_BANNER)
    Call<String> uploadVendorBanner(@Field("userID") String userID,
                                    @Field("banner") String banner);

    @FormUrlEncoded
    @POST(DELETE_VENDOR_BANNER_AUTH)
    Call<String> deleteVendorBannerAuth(@Field("userID") String userID,
                                        @Field("banner_id") String banner_id);

    @FormUrlEncoded
    @POST(GET_VENDOR_BANNERLIST)
    Call<String> getVendorBannerList(@Field("userID") String userID);

    @FormUrlEncoded
    @POST(GET_AREA_BANNERLIST)
    Call<String> getAreaBannerList(@Field("userID") String userID);

    String ORDER_TOKEN_AUTH = "topupPgAuth";
    String ADD_TOPUP_WALLET = "PaymentReceiveFreecash";

    @FormUrlEncoded
    @POST(ORDER_TOKEN_AUTH)
    Call<String> OrderTokenAuth(@Field("user_id") String user_id,
                                @Field("order_id") String order_id,
                                @Field("amount") String amount);

    @FormUrlEncoded
    @POST(ADD_TOPUP_WALLET)
    Call<String> PaymentReceiveFreecash(@Field("user_id") String MemberID,
                                        @Field("order_id") String order_id,
                                        @Field("amount") String Amount,
                                        @Field("payment_status") String PaymentStatus);

}
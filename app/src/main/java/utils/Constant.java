package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*Define all constant variables here
 */
public class Constant {

	/*Payment Avanue*/
	public static final String PARAMETER_SEP = "&";
	public static final String PARAMETER_EQUALS = "=";
	public static final String JSON_URL = "https://secure.ccavenue.com/transaction/transaction.do";
	public static final String TRANS_URL = "https://secure.ccavenue.com/transaction/initTrans";

	public static final int CONDIDATE_RAGISTRATION = 1;
	public static final int EMPLOYER_RAGISTRATION = 2;
	public static final int LOGIN = 3;
	public static final int LOGOUT = 4;
	public static final int FORGOTPASSWORD = 5;
	public static final int CHANGEPASSWORD = 6;
	public static final int SOCIALLOGIN = 7;
	public static final int GET_COMPANY_PROFILE = 8;
	public static final int GET_CANDIDATE_PROFILE = 9;
	public static final int UPDATE_CANDIDATE_PROFILE = 10;
	public static final int UPDATE_EMPLOYER_PROFILE = 11;
	public static final int UPDATE_EMPLOYER_PIC = 12;
	public static final int UPDATE_CADIDATE_PIC = 13;
	public static final int CADIDATE_TOP_TEN = 14;
	public static final int EMPLOYEETOP_TEN = 15;
	public static final int CANDIDATE_SEARCH_API = 16;
	public static final int COMPANY_SEARCH_API = 17;
	public static final int NOTIFICATION_API = 18;
	public static final int VIEW_PROFILE_API = 19;
	public static final int GET_FILTER_API = 20;
	public static final int GET_FILTER_DATA = 21;
	public static final int POST_JOB = 22;
	public static final int GET_ALL_POST = 23;
	public static final int GET_JOB_LIST = 24;
	//Sohel
	public static final int GET_PACK_LIST = 25;
	public static final int SELECT_PACK = 26;

	public static final int UPDATE_POST = 27;
	public static final int DELETE_POST = 28;

	public static final int APPLY_EMPLOYEE_FILTER = 29;
	public static final int APPLY_CUSTOMER_FILTER = 30;
	public static final int UPDATE_DWONLOAD = 31;
	public static final int UPDATE_APPLICANT = 32;

//	public static String BASE_URL ="http://smtgroup.in/jobpools/wdiapi/index.php/";
	public static String BASE_URL ="http://jobpool.in/jobpoolapi/index.php/";

	public static String CONDIDATE_RAGISTRATION_URL = BASE_URL+"candidate_register";
	public static String EMPLOYER_RAGISTRATION_URL = BASE_URL+"employer_register";
	public static String LOGIN_URL = BASE_URL+"user_login";
	public static String LOGOUT_URL = BASE_URL+"candidate_logout";
	public static String FORGOT_URL = BASE_URL+"candidate_forgot_password";
	public static String CHANGE_PASSWORD_URL = BASE_URL+"candidate_changed_password";
	public static String SOCIAL_LOGIN_URL = BASE_URL+"candidate_fblogin";
	public static String GET_PROFILE_URL = BASE_URL+"get_profile";
	public static String UPDATE_CANDIDATE_PROFILE_URL = BASE_URL+"candidate_update";
	public static String UPDATE_EMPLOYER_PROFILE_URL = BASE_URL+"employer_update";
	public static String UPDATE_EMPLOYER_PIC_URL = BASE_URL+"employer_profilepic";
	public static String UPDATE_CANDIDATE_PIC_URL = BASE_URL+"candidate_profilepic";
	public static String UPDATE_CANDIDATE_TOP_TEN_URL = BASE_URL+"topten_candidate";
	public static String UPDATE_EMPLOYEE_TOP_TEN_URL = BASE_URL+"topten_employer";
	public static String SEARCH_URL = BASE_URL+"candidate_search";
	public static String COMPANY_SEARCH_URL = BASE_URL+"company_search";
	public static String NOTIFICATION_URL = BASE_URL+"notificationlist";
	public static String VIEW_PROFILE_URL = BASE_URL+"viewprofile";
	public static String GET_FILTER_URL = BASE_URL+"getallcity";
	public static String POST_JOB_URL = BASE_URL+"postjob";
	public static String GET_ALL_POST_URL = BASE_URL+"allpostedjob";
	public static String GET_FILTER_DATA_URL = BASE_URL+"getallfilter";
	public static String GET_ALL_JOBLIST_URL = BASE_URL+"alljobrole";
	public static String GET_ALL_MEMBERSHIP_URL = BASE_URL+"allmembership";
	public static String SELECT_MEMBERSHIP_URL = BASE_URL+"update_membership";
	public static String UPDATE_POST_URL = BASE_URL+"updatepostjob";
	public static String DELETE_POST_URL = BASE_URL+"deletepostjob";
	public static String APPLY_FILTTER_URL = BASE_URL+"singalfilter";
	public static String APPLY_CUSTOMER_FILTTER_URL = BASE_URL+"employerfilter";
	public static String UPDATE_DOWNLOAD_URL = BASE_URL+"update_download";
	public static String UPDATE_APPLICANT_URL = BASE_URL+"update_applicant";

	public static String NAME="";
	public static String USER_NAME="";
	public static String EMAIL="";
	public static String PASSWORD="";
	public static String CONFIRM_PASSWORD="";
	public static String PHONE_NUMBER="";
	public static String GENDER="Male";
	public static String USER_ID="";
	public static String USER_IMAGE="";
	public static String DOCUMENT="";
	public static String RESUME="";
	public static String TYPE="";

	public static boolean JobFlage;

	/*SOCIAL LOGIN*/
	public static String FB_ID="";
	public static String GOOGLE_ID="";
	public static String COMPANY_ID="";
	public static String JOB_ID="";

	/*EMPLOYER REGISTER*/
	public static String COMPANY_NAME="";
	public static String CONTACTPERSON="";
	public static String CURRENT_REQUIRMENT="";
	public static String EXPERIENCE="";
	public static String SKILLES="";
	public static String JOBROLL="";
	public static String LOCATION="";
	public static String USER_TYPE="";
	public static String ADDRESS="";
	public static String NO_OF_REQUIRMENT="";
	public static String SPECILIZATION="";
	public static String TOKEN="";
	public static String BRANCH="";
	public static String DISCRIPTION="";
	public static String JOB_TYPE="";

	public static String OLDPASSWORD="";
	public static String PAYMENTAMOUNT="";

	public static String STRENGHT="";
	public static String EXP_SALARY="";
	public static String OBJECTIVE="";
	public static String BRIEFDESCRIPTION="";

	/*Maintain Posted jobs*/
	public static String OUT_OF_DOWNLOAD="";
	public static String NOOF_DOWNLOAD = "";
	public static String OUT_OF_POST = "";
	public static String NO_OF_POST = "";

	/*Maintain candiate data */
	public static String COMPANY_SHOW_INTERST="";
	public static String NO_OF_APPLIED="";
	public static String OUT_OFF_APPLY="";

	public static String PACKAGE_NAME = "";
	public static String PACKAGE_TYPE = "";


	public static String SELECTED_PACK="";

	public static String EMAIL_PETTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9][.com])?)*$";
	/*^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$*/

	public static boolean emailValidation(String emailtxt) {
		boolean isValid = false;
		CharSequence inputStr = emailtxt;
		Pattern pattern = Pattern.compile(Constant.EMAIL_PETTERN, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	public static boolean checkInternetConnection(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

	public static String gerDeviceId(Context context) {
		String android_id = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);

		return android_id;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		params.height = params.height+60;
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static String getDeviceName() {
		String str = android.os.Build.MODEL;
		return str;
	}

	public static int getDeviceWidth(Context context) {
		int width;

		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		width = displayMetrics.widthPixels;

		return width;
	}
}

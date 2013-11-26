package dogwin.net.user;import java.security.PublicKey;import android.R.integer;import android.annotation.SuppressLint;import android.annotation.TargetApi;import android.app.ActionBar;import android.app.ActionBar.OnNavigationListener;import android.app.ActionBar.Tab;import android.app.Activity;import android.app.Fragment;import android.app.FragmentTransaction;import android.app.Notification;import android.app.NotificationManager;import android.app.PendingIntent;import android.content.Intent;import android.content.IntentFilter;import android.content.SharedPreferences;import android.net.ConnectivityManager;import android.os.Build;import android.os.Bundle;import android.preference.EditTextPreference;import android.util.Log;import android.view.Menu;import android.view.MenuInflater;import android.view.MenuItem;import android.view.View;import android.widget.Button;import android.widget.EditText;import android.widget.Toast;import dogwin.net.apps.Buddha;import dogwin.net.apps.R;import dogwin.net.check.DwClient;import dogwin.net.check.Login;import dogwin.net.publics.Menus;@TargetApi(Build.VERSION_CODES.HONEYCOMB)@SuppressLint({ "InlinedApi", "ValidFragment", "NewApi" })public class Profile extends Activity{		int pid;	public boolean IconFlag=true,logout=false;	//check sharepreferences			public EditTextPreference editText;	public SharedPreferences preferences;	static String uc_username,uc_password;	static boolean uc_flag;	//menu use	Menus menus = new Menus();	@Override	protected void onCreate(Bundle savedInstanceState) {		// TODO Auto-generated method stub		super.onCreate(savedInstanceState);		//setContentView(R.layout.main_profile);		this.pid = android.os.Process.myPid();		uc_username = Rt_username();		uc_password = Rt_password();		uc_flag = Rt_flag();				final ActionBar actionBar = getActionBar();        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        String tab_userinfo,tab_lession,tab_friends;        tab_userinfo = getResources().getString(R.string.tab_userinfo);        tab_lession = getResources().getString(R.string.tab_lession);        tab_friends = getResources().getString(R.string.tab_friends);                Tab tabA = actionBar.newTab();        tabA.setText(tab_userinfo);        tabA.setTabListener(new TabListener<UserInfo>(this, tab_userinfo, UserInfo.class));        actionBar.addTab(tabA);                Tab tabB = actionBar.newTab();        tabB.setText(tab_lession);        tabB.setTabListener(new TabListener<Lessons>(this, tab_lession, Lessons.class));        actionBar.addTab(tabB);                Tab tabC = actionBar.newTab();        tabC.setText(tab_friends);        tabC.setTabListener(new TabListener<Friends>(this, tab_friends, Friends.class));        actionBar.addTab(tabC);                if (savedInstanceState != null) {            int savedIndex = savedInstanceState.getInt("SAVED_INDEX");            getActionBar().setSelectedNavigationItem(savedIndex);                    }			}	@Override	public boolean onCreateOptionsMenu(Menu menu) {		MenuInflater inflater = new MenuInflater(this);		if(DwClient.flag||uc_flag){			inflater.inflate(R.menu.menu_option_main, menu);		}else{			inflater.inflate(R.menu.menu_unlogin, menu);		}		return super.onCreateOptionsMenu(menu);			}	@Override	public boolean onOptionsItemSelected(MenuItem item) {		boolean menu_flag = false;		IconFlag = false;		if(DwClient.flag||uc_flag){			menu_flag = true;		}		return super.onOptionsItemSelected(menus.select_menus(item, Profile.this, pid, menu_flag));	}		@Override	protected void onStop() {		// TODO Auto-generated method stub		super.onStop();		Log.v("TAG", "back Run");				if(IconFlag){			createNotification();		}			}	@Override	protected void onRestart() {		// TODO Auto-generated method stub		super.onRestart();		IconFlag = false;	}	//Icon Notification		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)	@SuppressLint("NewApi")	public void createNotification(){		Intent intent = new Intent(this, Buddha.class);		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);		//title		String title = getResources().getString(R.string.app_name);		String subject = getResources().getString(R.string.subject);				Notification noti = new Notification.Builder(this)        .setContentTitle(title)        .setContentText(subject).setSmallIcon(R.drawable.ic_launcher)        .setContentIntent(pIntent).build();		 NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);		    // Hide the notification after its selected		 noti.flags |= Notification.FLAG_AUTO_CANCEL;		 notificationManager.notify(0, noti);	}		//get user current	public void SaveUC(String username,String password,boolean flag) {	    preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);	    SharedPreferences.Editor preferencesEditor = preferences.edit();	    preferencesEditor.putString("username", username);	    preferencesEditor.putString("password", password);	    preferencesEditor.putBoolean("flag", flag);	    preferencesEditor.commit();	}	public String Rt_username(){		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);	    return preferences.getString("username", null);	}			public String Rt_password(){		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);	    return preferences.getString("password", null);	}		public boolean Rt_flag(){		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);		return preferences.getBoolean("flag", false);	}		@Override	 protected void onSaveInstanceState(Bundle outState) {	  // TODO Auto-generated method stub	  super.onSaveInstanceState(outState);	  outState.putInt("SAVED_INDEX", getActionBar().getSelectedNavigationIndex());	 }	public static class TabListener<T extends Fragment> implements ActionBar.TabListener{				        private final Activity myActivity;        private final String myTag;        private final Class<T> myClass;        public TabListener(Activity activity, String tag, Class<T> cls) {            myActivity = activity;            myTag = tag;            myClass = cls;                    }         @Override  	public void onTabSelected(Tab tab, FragmentTransaction ft) {    	    	Fragment myFragment = myActivity.getFragmentManager().findFragmentByTag(myTag);      		// Check if the fragment is already initialized	     if (myFragment == null) {	         // If not, instantiate and add it to the activity	         myFragment = Fragment.instantiate(myActivity, myClass.getName());	         ft.add(android.R.id.content, myFragment, myTag);	         //System.out.println("myActivity=>"+myClass.getName().toString());	         	     } else {	         // If it exists, simply attach it in order to show it	         ft.attach(myFragment);	     }     	}    //      	@Override  	public void onTabUnselected(Tab tab, FragmentTransaction ft) {   	   Fragment myFragment = myActivity.getFragmentManager().findFragmentByTag(myTag);	   	   if (myFragment != null) {	             // Detach the fragment, because another one is being attached	             ft.detach(myFragment);	         }	   	  }		  @Override	  public void onTabReselected(Tab tab, FragmentTransaction ft) {	   // TODO Auto-generated method stub	  }	    }}
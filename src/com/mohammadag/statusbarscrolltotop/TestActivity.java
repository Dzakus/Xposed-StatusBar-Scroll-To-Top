package com.mohammadag.statusbarscrolltotop;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewFragment;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class TestActivity extends Activity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private static final String[] SECTION_NAME = new String[] {
			"WebView JS OFF", "WebView JS ON", "ScrollView",
			"HorizontalScrollView", "ListView" };
	private static final Fragment[] FRAGMENT = new Fragment[] {
			new CustomWebViewFragment(), new Custom2WebViewFragment(),
			new ScrollViewFragment(), new HorizontalScrollViewFragment(),
			new CustomListFragment() };
	private int frameId;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION_CODES.JELLY_BEAN <= Build.VERSION.SDK_INT) {
			frameId = View.generateViewId();
		} else {
			frameId = 49;
		}
		FrameLayout view = new FrameLayout(this);
		view.setId(frameId);
		setContentView(view);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		actionBar.setListNavigationCallbacks(
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, SECTION_NAME), this);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		Fragment fragment = FRAGMENT[position];
		getFragmentManager().beginTransaction().replace(frameId, fragment)
				.commit();
		return true;
	}

	public static class CustomWebViewFragment extends WebViewFragment {
		public CustomWebViewFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			WebView view = (WebView) super.onCreateView(inflater, container,
					savedInstanceState);

			StringBuilder data = new StringBuilder("<html><body>");
			data.append("<script>document.write(\"Javascrip Enabled!\");</script>");
			for (int i = 0; i < 100; i++) {
				data.append("</br></br>").append(i).append("<hr>");
			}
			data.append("</body></html>");
			view.loadData(data.toString(), "text/html", null);
			// view.loadDataWithBaseURL(null, html, "text/htm", "utf-8", null);
			return view;
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	public static class Custom2WebViewFragment extends CustomWebViewFragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			WebView view = (WebView) super.onCreateView(inflater, container,
					savedInstanceState);
			view.getSettings().setJavaScriptEnabled(true);
			return view;
		}

	}

	public static class ScrollViewFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ScrollView view = new ScrollView(getActivity());
			LinearLayout ll = new LinearLayout(getActivity());
			ll.setOrientation(LinearLayout.VERTICAL);
			for (int i = 0; i < 100; i++) {
				TextView tv = new TextView(getActivity());
				tv.setText("Test " + i + "#");
				tv.setPadding(0, 20, 0, 20);
				ll.addView(tv);
			}
			view.addView(ll);
			return view;
		}
	}

	public static class HorizontalScrollViewFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ViewGroup view = new HorizontalScrollView(getActivity());
			LinearLayout ll = new LinearLayout(getActivity());
			ll.setOrientation(LinearLayout.HORIZONTAL);
			for (int i = 0; i < 100; i++) {
				TextView tv = new TextView(getActivity());
				tv.setText("Test " + i + "# ");
				ll.addView(tv);
			}
			view.addView(ll);
			return view;
		}
	}

	public static class CustomListFragment extends ListFragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			String[] data = new String[100];
			for (int i = 0; i < 100; i++) {
				data[i] = "Test " + i + "#";
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_list_item_1, data);
			setListAdapter(adapter);
			return super.onCreateView(inflater, container, savedInstanceState);
		}
	}

}

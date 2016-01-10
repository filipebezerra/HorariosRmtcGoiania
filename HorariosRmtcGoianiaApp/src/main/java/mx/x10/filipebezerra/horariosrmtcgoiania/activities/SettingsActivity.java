package mx.x10.filipebezerra.horariosrmtcgoiania.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import de.psdev.licensesdialog.LicenseResolver;
import de.psdev.licensesdialog.LicensesDialogFragment;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import java.util.LinkedList;
import java.util.List;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.fragments.PreferenceCompatFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.DaoManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.utils.AndroidUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers.DialogHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers.SearchRecentSuggestionsHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers.SnackBarHelper;

import static android.preference.Preference.OnPreferenceClickListener;
import static mx.x10.filipebezerra.horariosrmtcgoiania.utils.PrefUtils.PREF_ABOUT_VERSION;
import static mx.x10.filipebezerra.horariosrmtcgoiania.utils.PrefUtils.PREF_CHANGELOG_INFO;
import static mx.x10.filipebezerra.horariosrmtcgoiania.utils.PrefUtils.PREF_CLEAR_FAVORITE_BUS_STOP_DATA;
import static mx.x10.filipebezerra.horariosrmtcgoiania.utils.PrefUtils.PREF_CLEAR_RECENT_SUGGESTIONS;
import static mx.x10.filipebezerra.horariosrmtcgoiania.utils.PrefUtils.PREF_LICENSES_INFO;
import static mx.x10.filipebezerra.horariosrmtcgoiania.utils.PrefUtils.PREF_MAIL_TO_DEVELOPER;
import static mx.x10.filipebezerra.horariosrmtcgoiania.utils.PrefUtils.PREF_TOUR;

/**
 * Preference screen for app preferences.
 *
 * @author Filipe Bezerra
 * @version 2.3, 09/01/2016
 * @since 2.0
 */
public class SettingsActivity extends ActionBarActivity {
    public static final String ID_BASIC_SETTINGS = "basic";
    public static final String ID_DEVELOPER_SETTINGS = "developer";
    public static final String ID_ABOUT_SETTINGS = "about";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupActionBar();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, new SettingsHeadersFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (!getSupportFragmentManager().popBackStackImmediate()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchToSettings(String settingsId) {
        Bundle args = new Bundle();
        args.putString(SettingsFragment.ARG_SETTINGS, settingsId);
        Fragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected static void setupBasicSettings(final Activity activity,
            Preference prefClearRecentSuggestions, Preference prefClearFavoriteBusStopData) {
        prefClearRecentSuggestions.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                MaterialDialog.Builder dialogBuilder = DialogHelper.build(activity,
                        R.string.pref_title_clear_recent_suggestions,
                        R.string.pref_prompt_clear_recent_suggestions,
                        R.string.erase_button,
                        R.string.cancel_button);
                dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        SearchRecentSuggestionsHelper.getInstance(activity).clearHistory();
                        preference.setSummary(activity.getString(
                                R.string.pref_summary_recent_suggestions_changed));
                        preference.setEnabled(false);
                    }
                });
                dialogBuilder.show();
                return true;
            }
        });

        prefClearFavoriteBusStopData.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                MaterialDialog.Builder dialogBuilder = DialogHelper.build(activity,
                        R.string.pref_title_clear_favorite_bus_stop_data,
                        R.string.pref_prompt_clear_favorite_bus_stop_data,
                        R.string.erase_button,
                        R.string.cancel_button);
                dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        DaoManager.getInstance(activity).getFavoriteBusStopDao().deleteAll();
                        preference.setSummary(activity.getString(
                                R.string.pref_summary_favorite_bus_stop_data_changed));
                        preference.setEnabled(false);

                        // TODO notify favorite bus stop adapter to clear data
                    }
                });
                dialogBuilder.show();
                return true;
            }
        });
    }

    protected static void setupDeveloperSettings(final Activity activity,
            Preference prefMailToDeveloper) {
        prefMailToDeveloper.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", activity.getString(R.string.developer_email), null));

                if (emailIntent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(Intent.createChooser(emailIntent,
                            activity.getString(R.string.send_mail)));
                } else {
                    SnackBarHelper.show(activity,
                            activity.getString(R.string.message_send_mail_application_not_found));
                }
                return true;
            }
        });
    }

    protected static void setupAboutSettings(final Activity activity,
            final FragmentManager fragmentManager, Preference prefAboutVersion,
            Preference prefLicensesInfo, Preference prefChangelogInfo, Preference prefTour) {
        final String version = AndroidUtils.getVersion(activity);
        prefAboutVersion.setSummary("v" + version);

        prefLicensesInfo.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                LicenseResolver.registerLicense(new ApacheSoftwareLicense20());
                try {
                    new LicensesDialogFragment
                            .Builder(activity)
                            .setNotices(R.raw.notices)
                            .setShowFullLicenseText(true)
                            .setIncludeOwnLicense(true)
                            .setThemeResourceId(R.style.MD_Light)
                            .setDividerColorRes(R.color.licenses_dialog_divider_color)
                            .build()
                            .show(fragmentManager, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        prefChangelogInfo.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new MaterialDialog.Builder(activity)
                        .title(R.string.pref_title_changelog_info)
                        .customView(R.layout.view_changelog, false)
                        .positiveText(R.string.ok_button)
                        .show();
                return true;
            }
        });
    }

    public static class SettingsHeadersFragment extends Fragment {
        private HeaderAdapter mHeaderAdapter;
        @Bind(R.id.listViewSettingsHeaders) protected ListView listView;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_settings_headers, container, false);
            ButterKnife.bind(this, view);
            return view;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            mHeaderAdapter = new HeaderAdapter(getActivity(), buildHeaders());
            listView.setAdapter(mHeaderAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Header header = mHeaderAdapter.getItem(position);
                    ((SettingsActivity)getActivity()).switchToSettings(header.settingsId);
                }
            });
        }

        private List<Header> buildHeaders() {
            List<Header> headers = new LinkedList<>();
            headers.add(new Header(R.string.prefs_category_basic, ID_BASIC_SETTINGS));
            headers.add(new Header(R.string.prefs_category_developer, ID_DEVELOPER_SETTINGS));
            headers.add(new Header(R.string.prefs_category_about, ID_ABOUT_SETTINGS));
            return headers;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.unbind(this);
        }

        private static class HeaderAdapter extends ArrayAdapter<Header> {
            private final LayoutInflater mInflater;

            private static class HeaderViewHolder {
                TextView title;

                public HeaderViewHolder(View view) {
                    title = (TextView) view.findViewById(R.id.textViewSettingsHeader);
                }
            }

            public HeaderAdapter(Context context, List<Header> headers) {
                super(context, 0, headers);
                mInflater = (LayoutInflater) context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                HeaderViewHolder viewHolder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_settings_header, parent, false);
                    viewHolder = new HeaderViewHolder(convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (HeaderViewHolder) convertView.getTag();
                }

                viewHolder.title.setText(getContext().getString(getItem(position).titleRes));

                return convertView;
            }
        }

        public static final class Header {
            public int titleRes;
            public String settingsId;

            public Header(int titleResId, String settingsId) {
                this.titleRes = titleResId;
                this.settingsId = settingsId;
            }
        }
    }

    public static class SettingsFragment extends PreferenceCompatFragment {
        public static final String ARG_SETTINGS = "settings";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            String settings = getArguments().getString(ARG_SETTINGS);

            switch (settings) {
                case ID_BASIC_SETTINGS:
                    addPreferencesFromResource(R.xml.settings_basic);
                    setupBasicSettings(getActivity(), findPreference(PREF_CLEAR_RECENT_SUGGESTIONS),
                            findPreference(PREF_CLEAR_FAVORITE_BUS_STOP_DATA));
                    break;
                case ID_DEVELOPER_SETTINGS:
                    addPreferencesFromResource(R.xml.settings_developer);
                    setupDeveloperSettings(getActivity(), findPreference(PREF_MAIL_TO_DEVELOPER));
                    break;
                case ID_ABOUT_SETTINGS:
                    addPreferencesFromResource(R.xml.settings_about);
                    setupAboutSettings(getActivity(), getActivity().getSupportFragmentManager(),
                            findPreference(PREF_ABOUT_VERSION),
                            findPreference(PREF_LICENSES_INFO),
                            findPreference(PREF_CHANGELOG_INFO),
                            findPreference(PREF_TOUR));
                    break;
            }
        }
    }
}
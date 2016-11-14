package com.korjus.todo;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static MainActivity main;
    public static String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = this;
        path = main.getFilesDir().getAbsolutePath();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private ArrayList<String> items;
        private ArrayAdapter<String> itemsAdapter;
        private ListView lvItems;
        private Button btn;
        String fileName;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            int pageNr = getArguments().getInt(ARG_SECTION_NUMBER);
            fileName = "todo" + String.valueOf(pageNr) + ".txt";

            lvItems = (ListView) rootView.findViewById(R.id.lvItems);
            btn = (Button) rootView.findViewById(R.id.btnAddItem);

            if (pageNr == 1){
                btn.setText("Work it!");
                btn.getBackground().setColorFilter(0xFFFF7474, PorterDuff.Mode.MULTIPLY);
            } else if (pageNr == 2){
                btn.setText("Do it!");
            } else if (pageNr == 3){
                btn.setText("Shop it!");
                btn.getBackground().setColorFilter(0xFFA9B6FF, PorterDuff.Mode.MULTIPLY);

            }

            items = new ArrayList<String>();
            readItems();
            itemsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item, items);
            lvItems.setAdapter(itemsAdapter);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText etNewItem = (EditText) getView().findViewById(R.id.etNewItem);
                    String itemText = etNewItem.getText().toString();
                    itemsAdapter.add(itemText);
                    etNewItem.setText("");
                    writeItems();
                }
            });

            lvItems.setOnItemLongClickListener(
                    new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapter,
                                                       View item, int pos, long id) {
                            // Remove the item within array at position
                            items.remove(pos);
                            // Refresh the adapter
                            itemsAdapter.notifyDataSetChanged();
                            // Return true consumes the long click event (marks it handled)
                            writeItems();
                            return true;
                        }

                    });

            return rootView;
        }
        private void readItems() {
            File todoFile = new File(MainActivity.path, fileName);
            try {
                if (todoFile.createNewFile()){
                    Log.d("u8i", "readItems: " + String.valueOf(1));
                }else{
                    Log.d("u8i", "readItems: " + String.valueOf(2));
                }

                items = new ArrayList<String>(FileUtils.readLines(todoFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void writeItems() {
            File todoFile = new File(MainActivity.path, fileName);
            try {
                FileUtils.writeLines(todoFile, items);
                Log.d("u8i", "writeItems: " + String.valueOf(todoFile.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "todo1.txt";
                case 1:
                    return "todo2.txt";
                case 2:
                    return "todo3.txt";
            }
            return null;
        }
    }
}

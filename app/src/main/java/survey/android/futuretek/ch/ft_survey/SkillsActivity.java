/**
 * Copyright (C) futuretek AG 2016
 * All Rights Reserved
 *
 * @author Artan Veliju
 */
package survey.android.futuretek.ch.ft_survey;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SkillsActivity extends BaseActivity {
    private Button btn_add;
    private Button nextBtn;
    private ListView listview;
    public List<String> _productlist = new ArrayList<String>();
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        listview = (ListView) findViewById(R.id.listView);
        View mainTextView = findViewById(R.id.textLayout);
        mainTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });


        btn_add = (Button) findViewById(R.id.insertBtn);
        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openInputDialog(new View.OnClickListener() {
                    public void onClick(View v) {
                        EditText userInput = ((EditText) v.findViewById(R.id.userInput));
                        String skill = null;
                        try {
                            skill = userInput.getText().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (!(skill == null || skill.isEmpty())) {
                            insertSkill(skill);
                        }
                    }
                });
            }
        });


        nextBtn = (Button) findViewById(R.id.nextBtn3);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(SkillsActivity.this, FinishActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((ViewGroup) findViewById(R.id.textLayout)).removeAllViews();
        List<String> textArray = new ArrayList<>(1);
        textArray.add("Please add a developer skill");
        animateText(textArray);
        _productlist.clear();
        _productlist = getDatabase().getAllSkills();
        adapter = new ListAdapter(this);
        listview.setAdapter(adapter);
    }

    private class ListAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ViewHolder viewHolder;

        public ListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return _productlist.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_row, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.textView1);
                viewHolder.delBtn = (Button) convertView.findViewById(R.id.deleteBtn);
                viewHolder.delBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ViewGroup row = ((ViewGroup) v.getParent());
                        String id = ((TextView) row.findViewById(R.id.textView1)).getText().toString();
                        getDatabase().deleteSkill(id);
                        _productlist.remove(id);
                        adapter.notifyDataSetChanged();
                    }
                });

                viewHolder.updBtn = (Button) convertView.findViewById(R.id.updateBtn);
                viewHolder.updBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ViewGroup row = ((ViewGroup) v.getParent());
                        final String id = ((TextView) row.findViewById(R.id.textView1)).getText().toString();

                        openInputDialog(id, new View.OnClickListener() {
                            public void onClick(View v) {
                                EditText userInput = ((EditText) v.findViewById(R.id.userInput));
                                String newValue = userInput.getText().toString();

                                updateSkill(id, newValue);
                            }
                        });
                    }
                });

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(_productlist.get(position));
            return convertView;
        }
    }

    private class ViewHolder {
        TextView textView;
        Button delBtn;
        Button updBtn;

    }

    private void insertSkill(String skill) {
        try {
            getDatabase().putSkill(skill);
            _productlist = getDatabase().getAllSkills();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSkill(String key, String skill) {
        try {
            getDatabase().updateSkill(key, skill);
            _productlist = getDatabase().getAllSkills();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void openInputDialog(String value, final View.OnClickListener onClickListener) {
        final Dialog dlg = new Dialog(this);
        dlg.setContentView(R.layout.dialog);

        ((TextView) dlg.findViewById(R.id.textView1)).setText("Update skill:");
        ((EditText) dlg.findViewById(R.id.userInput)).setText(value);
        try {
            ((Button) dlg.findViewById(R.id.okBtn)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dlg.dismiss();
                    onClickListener.onClick(dlg.getWindow().getDecorView());
                }
            });
            dlg.setOnCancelListener(
                    new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            dlg.dismiss();
                        }
                    });
            dlg.show();
        } catch (Exception e) {
        }
    }
}
package com.bmw.m2.views.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bmw.m2.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/2.
 */
public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {
    private List<File> files;
    private Context context;
    private boolean isPicture;
    private OnItemLongClickListener listener;
    private static final String TAG = "FileListAdapter";
    private boolean isChoose;
    private List<Integer> deleteChooseList;  //记录选择
    private int mOneChoose = -1;


    public List<Integer> getDeleteChooseList() {
        return deleteChooseList;
    }

    public void setChoose(boolean choose) {
        this.isChoose = choose;
//        if(choose)
//            clickChangeListener.Click(null);
        notifyDataSetChanged();
    }

    public void deleteOneChoose() {
        if (mOneChoose < 0 || mOneChoose >= files.size())
            return;
        files.get(mOneChoose).delete();
        files.remove(mOneChoose);
        if (files.size() > 0) {
            if (mOneChoose >= files.size())
                mOneChoose = files.size() - 1;
            if (clickChangeListener != null)
                clickChangeListener.Click(files.get(mOneChoose).getAbsolutePath());
        } else {
            mOneChoose = -1;
            if (clickChangeListener != null)
                clickChangeListener.Click(null);
        }
        notifyDataSetChanged();
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.listener = listener;
    }

    public FileListAdapter(Context context, boolean isPicture) {
        this.context = context;
        this.isPicture = isPicture;
        deleteChooseList = new ArrayList<>();
        files = new ArrayList<>();
    }

    public void setFiles(List<File> files) {
        if (files != null) {
            this.files = files;
        } else {
            this.files = null;
            this.files = new ArrayList<>();
        }
        mOneChoose = -1;
       /* if (clickChangeListener != null && files != null && files.size() > 0) {
            clickChangeListener.Click(files.get(0).getAbsolutePath());
        }*/
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.file_item, null);
        return new ViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.img.setTag(files.get(position).getName());
        if (isChoose) {//进入文件管理模式
            holder.img.setClickable(false);
            holder.bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (deleteChooseList.contains(position)) {
                        deleteChooseList.remove(deleteChooseList.indexOf(position));
                        if (deleteChooseList.size() == 0) {
                            isChoose = false;
                            if (clickChangeListener != null) {
                                clickChangeListener.cancelLongClick();
                                if (mOneChoose < files.size() && mOneChoose >= 0)
                                    clickChangeListener.Click(files.get(mOneChoose).getAbsolutePath());
                            }
                        }
                        notifyDataSetChanged();
                    } else {
                        deleteChooseList.add(position);
                        notifyDataSetChanged();
                    }
                }
            });

        }

//        if (isPicture) {
        if (files != null && files.size() > 0) {
            if (!isChoose) {
                holder.chooseImg.setVisibility(View.GONE);
                if (mOneChoose == position) {
                    holder.bg.setBackgroundColor(context.getResources().getColor(R.color.colorBase_half));
                } else {
                    holder.bg.setBackgroundColor(context.getResources().getColor(R.color.colorLucency));
                }
            } else {
                if (deleteChooseList != null && deleteChooseList.contains(position)) {
                    holder.chooseImg.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onBindViewHolder: " + position);
                    holder.bg.setBackgroundColor(context.getResources().getColor(R.color.colorBase_half));
                } else {
                    holder.chooseImg.setVisibility(View.GONE);
                    holder.bg.setBackgroundColor(context.getResources().getColor(R.color.colorLucency));
                }
            }
        }
       /* } else {

            if (deleteChooseList != null && deleteChooseList.contains(position)) {
                Log.d(TAG, "onBindViewHolder: " + position);
                holder.bg.setBackgroundColor(context.getResources().getColor(R.color.btn_press));
            } else {
                holder.bg.setBackgroundColor(context.getResources().getColor(R.color.background));
                Drawable mDrawableDefault = context.getResources().getDrawable(R.color.background);
                Drawable mDrawablePressed = context.getResources().getDrawable(R.color.btn_press);
                StateListDrawable drawable = new StateListDrawable();
                //按下状态
                drawable.addState(new int[]{android.R.attr.state_pressed}, mDrawablePressed);
                //普通状态
                drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected,
                        -android.R.attr.state_pressed}, mDrawableDefault);
                holder.bg.setBackgroundDrawable(drawable);
            }
        }*/
        if (isPicture) {
            String fName = files.get(position).getName();
            fName = fName.substring(0, fName.indexOf("."));
            holder.img.setImageResource(R.mipmap.picfile);
            holder.text.setText(fName);
        } else {
            holder.img.setImageResource(R.mipmap.video);
            String fName = files.get(position).getName();
            fName = fName.substring(0, fName.indexOf("."));
            holder.text.setText(fName);
        }

        if (!isChoose) {

            if (isPicture) {
                holder.bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mOneChoose = position;
                        notifyDataSetChanged();
                        if (clickChangeListener != null) {
                            clickChangeListener.Click(files.get(position).getAbsolutePath());
                        }
                        /*Log.d(TAG, "onClick: ");
                        Intent intent = new Intent(context, PicShowActivity.class);
                        intent.putExtra("bitmap",files.get(position).getAbsolutePath());
                        intent.putExtra("deleteChooseList", (Serializable) files);
                        intent.putExtra("position",position);
                        context.startActivity(intent);*/

                    }
                });

                holder.bg.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (!isChoose) {

                          /*  //打开指定的一张照片
                            Intent intent = new Intent();
                            intent.setAction(android.content.Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(files.get(position)), "image*//*");
                            context.startActivity(intent);*/

                            if (clickChangeListener != null) {
                                clickChangeListener.longClick(position);
                            }
//                            mOneChoose = position;
//                            notifyDataSetChanged();
                        }
                        return true;
                    }
                });


            } else {

                holder.bg.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Log.d(TAG, "onLongClick: ");
                        if (!isChoose) {
                            if (clickChangeListener != null) {
                                clickChangeListener.longClick(position);
                            }
                          /* Uri uri = Uri.parse("file://" + files.get(position).getAbsolutePath());
                            //调用系统自带的播放器
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uri, "video/mp4");
                            context.startActivity(intent);*/
//                            mOneChoose = position;
//                            notifyDataSetChanged();
                            /*Intent intent = null;
                            intent = new Intent(context, PlayerActivity.class);
                            intent.putExtra("path", files.get(position).getAbsolutePath());
                            context.startActivity(intent);*/
                        }
                        return true;
                    }
                });
                holder.bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOneChoose = position;

                        if (clickChangeListener != null) {
                            clickChangeListener.Click(files.get(position).getAbsolutePath());
                        }
                        mOneChoose = position;
                        notifyDataSetChanged();
                      /*   Intent intent = null;
                        intent = new Intent(context, PlayerActivity.class);
                        intent.putExtra("path", files.get(position).getAbsolutePath());
                        context.startActivity(intent);*/

                    }
                });
            }
//            holder.bg.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    Log.d(TAG, "onLongClick: ");
//                    if (!isChoose)
//                        listener.longClick(position);
//                    return true;
//                }
//            });
        }
    }


    public void chooseAll() {
        for (int i = 0; i < files.size(); i++) {
            if (!deleteChooseList.contains(i)) {
                System.out.println("adapter:choose = " + i);
                deleteChooseList.add(i);
                System.out.println("adapter:choose = " + deleteChooseList.toString() + " " + deleteChooseList.size());
            }
        }
        notifyDataSetChanged();
    }

    public void cancelAll() {
        for (int i = 0; i < files.size(); i++) {

            if (deleteChooseList.contains(i)) {
                deleteChooseList.remove(deleteChooseList.indexOf(i));

            }
        }
        notifyDataSetChanged();
    }

    public int deleteFile() {
        Log.d(TAG, "deleteFile1: " + deleteChooseList.toString());
        int n = deleteChooseList.size();
        for (Integer i : deleteChooseList) {
            deleteXmlFile(files.get(i).getAbsolutePath());
            files.get(i).delete();
        }
        cancelAll();
        Log.d(TAG, "deleteFile: " + deleteChooseList.toString());

        return n;
    }

    private void deleteXmlFile(String path) {
        if (isPicture) {
            String pathName = path.substring(0, path.lastIndexOf("."));
            File xmlFile = new File(pathName + ".xml");
            if (xmlFile.exists())
                xmlFile.delete();
        }
    }

    public int getListSize() {
        return deleteChooseList.size();
    }

    public void lastItem() {
        if (files == null)
            return;
        if (files.size() == 0)
            return;
        if (mOneChoose == 0)
            mOneChoose = files.size() - 1;
        else
            mOneChoose--;
        notifyDataSetChanged();
        if (clickChangeListener != null)
            clickChangeListener.Click(files.get(mOneChoose).getAbsolutePath());

    }

    public void nextItem() {
        if (files == null)
            return;
        if (files.size() == 0)
            return;
        if (mOneChoose == files.size() - 1)
            mOneChoose = 0;
        else
            mOneChoose++;
        notifyDataSetChanged();
        if (clickChangeListener != null)
            clickChangeListener.Click(files.get(mOneChoose).getAbsolutePath());

    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView text;
        private LinearLayout bg;
        private ImageView chooseImg;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_img);
            text = (TextView) itemView.findViewById(R.id.item_text);
            bg = (LinearLayout) itemView.findViewById(R.id.item);
            chooseImg = (ImageView) itemView.findViewById(R.id.item_choose);
        }
    }

    public interface OnItemLongClickListener {
        void longClick(int position);
    }


    public void addDeleteChooseList(int i) {
        deleteChooseList.add(i);
    }

    public interface OnClickChangeListener {
        void Click(String path);

        void longClick(int position);

        void cancelLongClick();
    }

    private OnClickChangeListener clickChangeListener;

    public void setOnClickChangeListener(OnClickChangeListener listener) {
        this.clickChangeListener = listener;
    }

}

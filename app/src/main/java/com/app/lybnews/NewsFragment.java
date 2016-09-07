package com.app.lybnews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.app.lybnews.model.Data;
import com.app.lybnews.model.Root;
import com.google.gson.Gson;

import loopj.android.image.SmartImageView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsFragment extends Fragment {

	private ViewPager mViewPaper;
	private List<ImageView> images;
	private List<View> dots;
	private int currentItem;
	// 记录上一次点的位置
	private int oldPosition = 0;
	// 存放图片的id
	private int[] imageIds = new int[] { R.drawable.pic1, R.drawable.pic2,
			R.drawable.pic3, R.drawable.pic4, R.drawable.pic5 };
	// 存放图片的标题
	private String[] titles = new String[] { "泛娱乐时代思考", "12年后女排再写传奇", "2016杭州G20峰会", "2016里约奥运闭幕",
			"洪荒少女傅园慧火了" };
	private TextView title;
	private ViewPagerAdapter adapter;
	private ScheduledExecutorService scheduledExecutorService;

	List<Data> newsList;
	ListView lv;
	Root rootNews;
	Data data;
	/**
	 * 接收子线程传递过来的数据
	 */

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					mViewPaper.setCurrentItem(currentItem);
					break;
				case 1:
					lv.setAdapter(new MyAdapter());
					break;
			}

		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View newsView = inflater
				.inflate(R.layout.newsfragment, container, false);// 关联布局文件
		mViewPaper = (ViewPager) newsView.findViewById(R.id.vp);
		lv = (ListView) newsView.findViewById(R.id.lv);
		// 显示的图片
		images = new ArrayList<ImageView>();
		for (int i = 0; i < imageIds.length; i++) {
			ImageView imageView = new ImageView(NewsFragment.this.getActivity());
			imageView.setBackgroundResource(imageIds[i]);
			images.add(imageView);
		}

		// 显示的小点
		dots = new ArrayList<View>();
		dots.add(newsView.findViewById(R.id.dot_0));
		dots.add(newsView.findViewById(R.id.dot_1));
		dots.add(newsView.findViewById(R.id.dot_2));
		dots.add(newsView.findViewById(R.id.dot_3));
		dots.add(newsView.findViewById(R.id.dot_4));

		title = (TextView) newsView.findViewById(R.id.title);
		title.setText(titles[0]);

		adapter = new ViewPagerAdapter();
		mViewPaper.setAdapter(adapter);

		mViewPaper
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						title.setText(titles[position]);
						dots.get(position).setBackgroundResource(
								R.drawable.dot_focused);
						dots.get(oldPosition).setBackgroundResource(
								R.drawable.dot_normal);

						oldPosition = position;
						currentItem = position;
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});
		getNewsInfo();

		return newsView;
	}

	@SuppressWarnings("unchecked")
	private void init() {

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Data data = newsList.get(position);
				String newsUrl = data.getUrl();
				Intent intent = new Intent(NewsFragment.this.getActivity(),DetailsActivity.class);
				intent.putExtra("extra",position);
				intent.putExtra("extra1",newsUrl);
				startActivity(intent);
			}
		});
	}

	/**
	 * 自定义Adapter
	 */
	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			view.removeView(images.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			view.addView(images.get(position));
			return images.get(position);
		}

	}

	/**
	 * 利用线程池定时执行动画轮播
	 */
	@Override
	public void onStart() {
		super.onStart();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleWithFixedDelay(new ViewPageTask(), 2,
				5, TimeUnit.SECONDS);
	}

	private class ViewPageTask implements Runnable {

		@Override
		public void run() {
			currentItem = (currentItem + 1) % imageIds.length;

			handler.sendEmptyMessage(0);
		}
	}

//	@Override
//	public void onStop() {
//		super.onStop();
//	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(scheduledExecutorService != null){
			scheduledExecutorService.shutdown();
			scheduledExecutorService = null;
		}
	}

	class MyAdapter extends BaseAdapter{
		//得到模型层中元素的数量，用来确定listview需要有多少个条目
		@Override
		public int getCount(){
			newsList = rootNews.getResult().getData();
			return newsList.size();
		}

		@Override
		//返回一个View对象，作为listview的条目显示至界面
		public View getView(int position, View convertView, ViewGroup parent) {
			data = newsList.get(position);

			View v = null;
			ViewHolder mHolder;
			if(convertView == null){
				v = View.inflate(NewsFragment.this.getActivity(), R.layout.news_item, null);
				mHolder = new ViewHolder();
				//把布局文件中所有组件的对象封装至ViewHolder对象中
				mHolder.tv_title = (TextView) v.findViewById(R.id.tv_title);
				mHolder.tv_date = (TextView) v.findViewById(R.id.tv_date);
				mHolder.tv_realtype = (TextView) v.findViewById(R.id.tv_realtype);
				mHolder.thumbnail_pic_s = (SmartImageView) v.findViewById(R.id.thumbnail_pic_s);
				//把ViewHolder对象封装至View对象中
				v.setTag(mHolder);
			}
			else{
				v = convertView;
				mHolder = (ViewHolder) v.getTag();
			}
			//给三个文本框设置内容
			mHolder.tv_title.setText(data.getTitle());
			mHolder.tv_date.setText(data.getDate());
			mHolder.tv_realtype.setText(data.getRealtype());
			//给新闻图片imageview设置内容
			mHolder.thumbnail_pic_s.setImageUrl(data.getThumbnail_pic_s());
			init();
			return v;
		}

		class ViewHolder{
			TextView tv_title;
			TextView tv_date;
			TextView tv_realtype;
			SmartImageView thumbnail_pic_s;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}
	private void getNewsInfo() {
		Thread t = new Thread(){
			@Override
			public void run() {
				String path = "http://v.juhe.cn/toutiao/index?type=top&key=6928eda123d4aef596b726b4addadf48";
				BufferedReader br = null;
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					//发送http GET请求，获取相应码
					if(conn.getResponseCode() == 200){
						br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String datas = "";
						String line = null;
						while ((line=br.readLine())!=null) {
							datas += line;
						}
						parseNewsJson(datas);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					if(br!=null){
						try{
							br.close();
						}catch(IOException e){
							e.printStackTrace();
						}
					}
				}
			}
		};
		t.start();
	}

	private void parseNewsJson(String datas) {

			Gson gson = new Gson();
			rootNews = gson.fromJson(datas, Root.class);
			if(rootNews.getReason().equals("成功的返回")){
				//发消息，让主线程设置listview的适配器，如果消息不需要携带数据，可以发送空消息
				handler.sendEmptyMessage(1);
			}else{
				Toast.makeText(NewsFragment.this.getActivity(),"解析失败",Toast.LENGTH_SHORT);
			}
	}
}

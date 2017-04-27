package com.dong.android.ui.test;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.dong.android.R;
import com.dong.android.base.presenter.BasePresenter;
import com.dong.android.base.view.BaseActivity;
import com.dong.android.widget.SwipeRefreshView;
import com.dong.recycler.BaseViewHolder;
import com.dong.recycler.CommonRecyclerAdapter;
import com.dong.recycler.ItemTypeEntity;
import com.dong.recycler.manage.ItemTypeManage;
import com.dong.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

/**
 * @author <dr_dong>
 * @time 2017/4/25 17:45
 */
public class TestRecyclerActivity extends BaseActivity {

    public static final String TAG = TestRecyclerActivity.class.getSimpleName();

    @BindView(R.id.test_srl)
    SwipeRefreshView swipeRefreshView;
    @BindArray(R.array.loading_colors)
    int[] loadingColors;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ItemTypeManage itemTypeManage;
    private List<TestData> dataList;
    private TestAdapter adapter;

    @Override
    protected int getRootView() {
        return R.layout.activity_test_recycler;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void setListener() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            //TODO refresh data
            UIUtils.showToast("refresh data");
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
            }, 2000);

        });
        swipeRefreshView.setOnLoadListener(() -> {
//            UIUtils.showToast("load more data");
            new Handler().postDelayed(() -> {
//                for (int i = 100; i < 120; i++) {
//                    dataList.add(new TestData(10000 * i, "title ==== " + i, "src ==== " + i, "name === " + i, i));
//                }
//                adapter.notifyDataSetChanged();
                swipeRefreshView.setLoadingClose();
            }, 2000);
        });

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        swipeRefreshLayout = swipeRefreshView.getRefreshLayout();
        swipeRefreshLayout.setColorSchemeColors(loadingColors);
        swipeRefreshView.setEnabled(true);
        recyclerView = swipeRefreshView.getRecyclerView();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        itemTypeManage = new ItemTypeManage<TestData>() {
            @Override
            protected int getItemType(TestData testData) {
                return testData.getItemType();
            }
        };
        itemTypeManage.registerItemType(0, R.layout.item_test_recycler_0);
        itemTypeManage.registerItemType(1, R.layout.item_test_recycler_1);
        itemTypeManage.registerItemType(2, R.layout.item_test_recycler_2);

        dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add(new TestData(10000 * i, "title ==== " + i, "src ==== " + i, "name === " + i, i));
        }

        adapter = new TestAdapter(itemTypeManage, dataList, recyclerView);
        recyclerView.setAdapter(adapter);
    }

    class TestAdapter extends CommonRecyclerAdapter<TestData> {

        public TestAdapter(@NonNull ItemTypeManage<TestData> itemTypeManage,
                           @NonNull List<TestData> data, @NonNull RecyclerView recyclerView) {
            super(itemTypeManage, data, recyclerView);
        }

        @Override
        protected BaseViewHolder convert(BaseViewHolder holder, TestData item) {
            switch (item.getItemType()) {
                case 0:
                    holder.setText(R.id.test_0_1, item.getName())
                            .setText(R.id.test_0_2, item.getTitle());
                    break;
                case 1:
                    holder.setText(R.id.test_1_1, item.getName())
                            .setText(R.id.test_1_2, item.getSrc());
                    break;
                case 2:
                    holder.setText(R.id.test_2_1, item.getSrc())
                            .setText(R.id.test_2_2, item.getTitle());
                    break;
            }
            return holder;
        }
    }

    class TestData implements ItemTypeEntity {

        long id;
        String title;
        String src;
        String name;
        int age;

        public TestData(long id, String title, String src, String name, int age) {
            this.id = id;
            this.title = title;
            this.src = src;
            this.name = name;
            this.age = age;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public int getItemType() {
            return age % 3;
        }
    }
}

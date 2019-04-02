package com.littlefox.storybook.lib.adapter.decoration;

import com.littlefox.logmonitor.Log;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration
{
	private int thumbnailMaxSize = 0;
	private int spanCount;

	private int horizontalSpacing = 0;
	private int verticalSpacing = 0;

	public GridSpacingItemDecoration(int spanCount, int spacing,  int itemMaxSize)
	{
		this.spanCount = spanCount;
		this.thumbnailMaxSize = itemMaxSize;
		
		horizontalSpacing = spacing;
		verticalSpacing = spacing;
	}
	

	
	/**
	 * 단행본 그리드뷰의 각각의 썸네일의 Spacing 작업을 위한 Decoration
	 * @param spanCount 한 라인에 보여야할 썸네일 개수
	 * @param horizontalSpacing 썸네일의 양 옆의 간격
	 * @param verticalSpacing 썸네일의 위아래의 간격
	 * @param addHorizontalEdgeSpacing 맨 왼쪽과 맨 오른쪽에 추가 되어야할 간격
	 * @param includeEdge 상단에 Spacing이 추가 될것인가 의 여부
	 * @param itemMaxSize 보여줄 썸네일의 사이즈 
	 */
	public GridSpacingItemDecoration(int spanCount, int horizontalSpacing, int verticalSpacing, int itemMaxSize)
	{
		this.spanCount = spanCount;

		this.thumbnailMaxSize = itemMaxSize;
		
		this.horizontalSpacing = horizontalSpacing;
		this.verticalSpacing = verticalSpacing;
	}
	



	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
	{
		int position = parent.getChildAdapterPosition(view); // item position
		int column = 0;

		if(position > 0 && position <= thumbnailMaxSize)
		{
			column = (position - 1) % spanCount; // item column
			
			outRect.left = horizontalSpacing - column * horizontalSpacing / spanCount;
			outRect.right = (column + 1) * horizontalSpacing / spanCount;
			
			outRect.bottom = verticalSpacing; 
		}

		
	}

}
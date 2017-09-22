package com.czc.max.taglayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import com.czc.max.taglayout.factory.TagFactory;
import com.czc.max.taglayout.interfaces.ITag;
import com.czc.max.taglayout.interfaces.OnTagItemClickListener;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * @author maxczc
 * @since 20/9/17
 */

public class TagLayout extends ViewGroup {

  public static final int LEFT = 0;
  public static final int CENTER_HORIZONTAL = 1;
  public static final int RIGHT = 2;
  private static final int DEFAULT_LINE_SPACING = 20;
  private static final int DEFAULT_TAG_SPACING = 20;

  @GRAVITY private int gravity;//默认居左
  private int lineSpacing;//行间距
  private int tagSpacing;//tag的间隔
  private SparseIntArray changePoints;
  private Class<? extends View> customView;
  private OnTagItemClickListener onTagItemClickListener;

  public TagLayout(Context context) {
    this(context, null);
  }

  public TagLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  @SuppressWarnings("WrongConstant")
  public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    changePoints = new SparseIntArray();
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagLayout);
    gravity = typedArray.getInt(R.styleable.TagLayout_gravity, LEFT);
    lineSpacing =
        typedArray.getDimensionPixelSize(R.styleable.TagLayout_lineSpacing, DEFAULT_LINE_SPACING);
    tagSpacing =
        typedArray.getDimensionPixelSize(R.styleable.TagLayout_tagSpacing, DEFAULT_TAG_SPACING);
    typedArray.recycle();
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    switch (gravity) {
      case LEFT:
        leftLayout(r - l);
        break;
      case CENTER_HORIZONTAL:
      case RIGHT:
        centerHorizontalOrRightLayout(r - l);
        break;
    }
  }

  /**
   * layout childrens when gravity is center_horizontal
   *
   * @param parentWidth parent width
   */
  private void centerHorizontalOrRightLayout(int parentWidth) {
    int childCount = getChildCount();
    int paddingLeft = getPaddingLeft() + 1;
    int paddingRight = getPaddingRight() + 1;
    int paddingTop = getPaddingTop();
    int leftPos = 0;
    int topPos = 0;
    int key = 0;
    int childWidth;
    int childHeight;
    for (int i = 0; i < childCount; i++) {
      View view = getChildAt(i);
      childWidth = view.getMeasuredWidth();
      if (childWidth > parentWidth - paddingLeft - paddingRight - leftPos) {
        leftPos = childWidth + tagSpacing;
        key++;
        changePoints.put(key, leftPos);
      } else {
        leftPos += childWidth + tagSpacing;
        changePoints.put(key, leftPos);
      }
    }
    leftPos = 0;
    int startPosition;
    int temp = 0;
    if (gravity == RIGHT) {
      startPosition = parentWidth - changePoints.get(temp) + tagSpacing - paddingRight;
    } else {
      startPosition = (parentWidth + tagSpacing - changePoints.get(temp)) / 2;
    }
    for (int i = 0; i < childCount; i++) {
      View view = getChildAt(i);
      childWidth = view.getMeasuredWidth();
      childHeight = view.getMeasuredHeight();
      if (childWidth > parentWidth - paddingLeft - paddingRight - leftPos) {
        leftPos = 0;
        topPos += childHeight + lineSpacing;
        temp++;
        if (gravity == RIGHT) {
          startPosition = parentWidth - changePoints.get(temp) + tagSpacing - paddingRight;
        } else {
          startPosition = (parentWidth + tagSpacing - changePoints.get(temp)) / 2;
        }
        view.layout(leftPos + startPosition, topPos + paddingTop,
            leftPos + childWidth + startPosition, topPos + childHeight + paddingTop);
        leftPos += childWidth + tagSpacing;
      } else {
        view.layout(leftPos + startPosition, topPos + paddingTop,
            startPosition + leftPos + childWidth, topPos + childHeight + paddingTop);
        leftPos += childWidth + tagSpacing;
      }
    }
  }

  private void leftLayout(int parentWidth) {
    int childCount = getChildCount();
    int paddingLeft = getPaddingLeft();
    int paddingRight = getPaddingRight();
    int paddingTop = getPaddingTop();
    int leftPos = 0;
    int topPos = 0;
    for (int i = 0; i < childCount; i++) {
      View view = getChildAt(i);
      int childWidth = view.getMeasuredWidth();
      int childHeight = view.getMeasuredHeight();
      if (childWidth > parentWidth - paddingLeft - paddingRight - leftPos) {
        topPos += childHeight + lineSpacing;
        view.layout(paddingLeft, topPos + paddingTop, paddingLeft + childWidth,
            topPos + childHeight + paddingTop);
        leftPos = childWidth + tagSpacing;
      } else {
        view.layout(leftPos + paddingLeft, topPos + paddingTop, leftPos + childWidth + paddingLeft,
            topPos + childHeight + paddingTop);
        leftPos += childWidth + tagSpacing;
      }
    }
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int count = getChildCount();
    int paddingLeft = getPaddingLeft();
    int paddingRight = getPaddingRight();
    int leftPos = paddingLeft, topPos = getPaddingTop();
    int height = 0;
    for (int i = 0; i < count; i++) {
      View view = getChildAt(i);
      int childWidth = view.getMeasuredWidth();
      int childHeight = view.getMeasuredHeight();
      if (childWidth > getMeasuredWidth() - leftPos - paddingRight) {
        height += childHeight + lineSpacing;
        leftPos = paddingLeft + childWidth + tagSpacing;
        topPos += childHeight + lineSpacing;
      } else {
        height = topPos + childHeight + getPaddingBottom();
        leftPos += childWidth + tagSpacing;
      }
      measureChild(view, widthMeasureSpec, heightMeasureSpec);
    }
    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
        resolveSize(height, heightMeasureSpec));
  }

  @IntDef({ LEFT, CENTER_HORIZONTAL, RIGHT }) @Retention(RetentionPolicy.SOURCE)
  @interface GRAVITY {
  }

  /**
   * 设置对齐方式
   *
   * @param gravity gravity
   */
  public void setGravity(@GRAVITY int gravity) {
    this.gravity = gravity;
    requestLayout();
  }

  /**
   * 设置行间距
   *
   * @param spacing 行间距
   */
  public void setLineSpacing(int spacing) {
    this.lineSpacing = spacing;
    requestLayout();
  }

  /**
   * 设置tag间距
   * @param spacing spacing
   */
  public void setTagSpacing(int spacing) {
    this.tagSpacing = spacing;
    requestLayout();
  }

  public void addAllTags(List tags) {
    for (Object tag : tags) {
      this.addTag(tag);
    }
  }

  @SuppressWarnings("unchecked") public void addTag(final Object tag) {
    if (customView != null) {
      View iTag = TagFactory.create(getContext(), customView);
      if (iTag instanceof ITag) {
        this.addView(iTag);
        iTag.setOnClickListener(new OnClickListener() {
          @Override public void onClick(View v) {
            if (onTagItemClickListener != null) {
              onTagItemClickListener.onItemClick(tag);
            }
          }
        });
        ((ITag) iTag).setValue(tag);
      } else {
        throw new IllegalArgumentException("the custom tag must implements ITag");
      }
    } else {
      TagView tagView = new TagView(getContext());
      tagView.setClickable(true);
      tagView.setValue((String) tag);
      tagView.setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          if (onTagItemClickListener != null) {
            onTagItemClickListener.onItemClick(tag);
          }
        }
      });
      this.addView(tagView);
    }
  }

  /**
   * 设置自定义tag
   *
   * @param view tag
   */
  public void setCustomTag(Class<? extends View> view) {
    this.customView = view;
  }

  public void setOnTagItemClickListener(OnTagItemClickListener listener) {
    this.onTagItemClickListener = listener;
  }

  /**
   * 默认tag
   */
  class TagView extends AppCompatTextView implements ITag<String> {

    public TagView(Context context) {
      super(context);
      setBackgroundResource(R.drawable.shape_label);
      setPadding(30, 15, 30, 15);
      setGravity(Gravity.CENTER);
      setEllipsize(TextUtils.TruncateAt.END);
      this.setLayoutParams(
          new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    @Override public void setValue(String s) {
      setText(s);
    }
  }
}

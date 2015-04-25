package huahua.mycontacts;

import android.content.Context;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/*
 * �Զ����󻬴�绰,�һ������ŵ�ViewPager
 */
public class ItemsViewPager extends ViewPager{

	private float xDown;//��¼��ָ����ʱ�ĺ����ꡣ
	private float xMove;//��¼��ָ�ƶ�ʱ�ĺ����ꡣ
	private float yDown;//��¼��ָ����ʱ�������ꡣ
	private float yMove;//��¼��ָ�ƶ�ʱ�������ꡣ
	private boolean viewpagersroll = false;//��ǰ�Ƿ���viewpager����
	
    public ItemsViewPager(Context context) {  
        super(context);  
    }  
      
    public ItemsViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  

	@Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
    	if(ev.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		//��¼����ʱ��λ��
    		xDown = ev.getRawX();
    		yDown = ev.getRawY();
    	}
    	else if(ev.getAction() == MotionEvent.ACTION_MOVE)
    	{
    		xMove = ev.getRawX();
    		yMove = ev.getRawY();
    		
    		if(viewpagersroll)
    		{
    			Log.i("huahua", "viewpager�Լ�������Ч��");
    			getParent().requestDisallowInterceptTouchEvent(true);
    			return super.dispatchTouchEvent(ev); 
    		}
    		
    		//����Ķ����ж���Viewpager����,ListView������
    		if(Math.abs(yMove - yDown) < 5 && Math.abs(xMove - xDown) > 20)
    		{
    			viewpagersroll = true;
    		}
    		else
    		{
    			Log.i("huahua", "�ɸ�listview��������Ч��");
    			return false;
    		}
    	
    	}
    	else if(ev.getAction() == MotionEvent.ACTION_UP)
    	{
    		viewpagersroll = false;
    	}
    	
    	return super.dispatchTouchEvent(ev); 
    }

	
}

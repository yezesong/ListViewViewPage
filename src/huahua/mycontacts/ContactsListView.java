package huahua.mycontacts;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

/*
 * �Զ�����ϵ�˵�ListView
 */

public class ContactsListView extends ListView {
	//ѡ�е����ĸ���ϵ�˵�����
	int m_chooseposition;
	public ContactsListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
    	if(ev.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		//��¼���µ����ĸ���ϵ��
 		   int x = (int) ev.getX();
 		   int y = (int) ev.getY();
 		  m_chooseposition = pointToPosition(x, y);
 		   Log.v("huahua", "SelectedItemId1111 = " + m_chooseposition);
    	}
    	else if(ev.getAction() == MotionEvent.ACTION_MOVE)
    	{
    		
    	}
    	else if(ev.getAction() == MotionEvent.ACTION_UP)
    	{
    		
    	}

		return super.dispatchTouchEvent(ev);
	}

}

package huahua.mycontacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	//��ϵ�˰�������Ϣ
	public static class Persons {
		public String Name;  //����
		public String PY;      //����ƴ�� (��������:huahuadashen)
		public String Number;      //�绰����
		public String FisrtSpell;      //����������ĸ (��������:hhds)
	} 
	//��ĸ����ͼView
	private AlphabetScrollBar m_asb;
	//��ʾѡ�е���ĸ
	private TextView m_letterNotice;
	//��ϵ�˵��б�
	private ContactsListView m_contactslist;
	//��ϵ���б��������
	private ListAdapter m_listadapter;
	//������ϵ������
	private ArrayList<Persons> persons = new ArrayList<Persons>();
	//����������ϵ��EditText
	private EditText m_FilterEditText;
	//û��ƥ����ϵ��ʱ��ʾ��TextView
	private TextView m_listEmptyText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//��ȡ�ֻ��е���ϵ��,����������ϵ�˱���perosns������
		//��ϵ�˱Ƚ϶�Ļ�,��ʼ���л�ȽϺ�ʱ,�Ժ����Ż�
		getContacts();
		//�õ���ĸ�еĶ���,�����ô�����Ӧ������
		m_asb = (AlphabetScrollBar)findViewById(R.id.alphabetscrollbar);
		m_asb.setOnTouchBarListener(new ScrollBarListener());
		m_letterNotice = (TextView)findViewById(R.id.pb_letter_notice);
		m_asb.setTextView(m_letterNotice);
		
		// ����ƴ��Ϊ��ϵ�������������
		Collections.sort(persons, new ComparatorPY());
		
		//�õ���ϵ���б�,������������
		m_contactslist = (ContactsListView)findViewById(R.id.pb_listvew);
		m_listadapter = new ListAdapter(this, persons);
		m_contactslist.setAdapter(m_listadapter);
		m_contactslist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.i("huahua",""+arg2);
				
			}
		});
		
		m_listEmptyText = (TextView)findViewById(R.id.pb_nocontacts_notice);
		
    	//��ʼ�������༭��,�����ı��ı�ʱ�ļ�����
		m_FilterEditText = (EditText)findViewById(R.id.pb_search_edit);
		m_FilterEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
					
				if(!"".equals(s.toString().trim()))
				{  
					//���ݱ༭��ֵ������ϵ�˲�������ϵ�б�
					filterContacts(s.toString().trim());
					m_asb.setVisibility(View.GONE);
				}
				else
				{
					m_asb.setVisibility(View.VISIBLE);
					m_listadapter.updateListView(persons);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public class ComparatorPY implements Comparator<Persons>{

		@Override
		public int compare(Persons lhs, Persons rhs) {
			String str1 = lhs.PY;
			String str2 = rhs.PY;
			return str1.compareToIgnoreCase(str2);
		}
	}
	
	public class ListAdapter extends BaseAdapter{
		private LayoutInflater m_inflater;
		private ArrayList<Persons> Persons;
    	private Context context;
    	
    	//��Ҫ��ӵ�Viewpager�е�3��View
		private View view1;
		private View view2;
		private View view3;
		private List<View> views;
		
        public ListAdapter(Context context,
        		ArrayList<Persons> persons) {
    	    this.m_inflater = LayoutInflater.from(context);
    	    this.Persons = persons;
    	    this.context = context;
        }
        
    	//����ϵ���б����ݷ����仯ʱ,�ô˷����������б�
    	public void updateListView(ArrayList<Persons> persons){
    		this.Persons = persons;
    		notifyDataSetChanged();
    	}

		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Persons.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return Persons.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null)
			{
				convertView = m_inflater.inflate(R.layout.vp_item, null);
			}
			
			views = new ArrayList<View>();
			
			view1 = LayoutInflater.from(context).inflate(R.layout.fragment1, null);
			view2 = LayoutInflater.from(context).inflate(R.layout.list_item, null);
			view3 = LayoutInflater.from(context).inflate(R.layout.fragment3, null);
			views.add(view1);
			views.add(view2);
			views.add(view3);
            
            TextView name = (TextView) view2.findViewById(R.id.contacts_name);
            name.setText(Persons.get(position).Name);
    	    
            TextView number = (TextView) view2.findViewById(R.id.contacts_number);
            number.setText(Persons.get(position).Number);
            
            //ÿ��listview��item����һ��Viewpager
            ItemsViewPager vp = (ItemsViewPager) convertView
				.findViewById(R.id.tabcontent_vp);
            //��viewpager����������
			vp.setAdapter(new TabAdapter());
			//��viewpager���û���ҳ������
			vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
			{
				@Override
				public void onPageSelected(int position)
				{
					if(position == 0)//��绰
					{
						//��һ��
						Vibrator vib = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
						vib.vibrate(50);
						
						Intent  intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel://" + Persons.get(m_contactslist.m_chooseposition).Number));
						startActivity(intent);
					}
					else if(position == 2)//������
					{
						Vibrator vib = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
						vib.vibrate(50);
						
						Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto://" + Persons.get(m_contactslist.m_chooseposition).Number));
							startActivity(intent);
					}
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					
				}
			});
			//��һ�μ��ض������õ���ʾ��2��ҳ��
			vp.setCurrentItem(1);
			
			//��ĸ��ʾtextview����ʾ 
			TextView letterTag = (TextView)convertView.findViewById(R.id.pb_item_LetterTag);
			//��õ�ǰ������ƴ������ĸ
			String firstLetter = Persons.get(position).PY.substring(0,1).toUpperCase();
			
			//����ǵ�1����ϵ�� ��ôletterTagʼ��Ҫ��ʾ
			if(position == 0)
			{
				letterTag.setVisibility(View.VISIBLE);
				letterTag.setText(firstLetter);
			}			
			else
			{
				//�����һ��������ƴ������ĸ
				String firstLetterPre = Persons.get(position-1).PY.substring(0,1).toUpperCase();
				//�Ƚ�һ�������Ƿ���ͬ
				if(firstLetter.equals(firstLetterPre))
				{
					letterTag.setVisibility(View.GONE);
				}
				else 
				{
					letterTag.setVisibility(View.VISIBLE);
					letterTag.setText(firstLetter);
				}
			}
			
			return convertView;
		}
		
		private class TabAdapter extends PagerAdapter
		{
			@Override
			public int getCount()
			{
				return views.size();
			}
			
			@Override
			public Object instantiateItem(View arg0, int arg1)
			{
	            ViewGroup p = (ViewGroup) views.get(arg1).getParent();  
	            if (p != null) {  
	                p.removeView(views.get(arg1));
	            }  
				
				((ViewPager) arg0).addView(views.get(arg1));
				
				return views.get(arg1);
			}
			
			@Override
			public void destroyItem(View arg0, int arg1, Object arg2)
			{
				
				((ViewPager) arg0).removeView(views.get(arg1));
			}
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1)
			{
				return arg0 == arg1;
			}
			
			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1)
			{
			}
			
			@Override
			public Parcelable saveState()
			{
				return null;
			}
			
			@Override
			public void startUpdate(View arg0)
			{
			}
			
			@Override
			public void finishUpdate(View arg0)
			{
			}
		}
		
	}
	
	//��ĸ�д����ļ�����
	private class ScrollBarListener implements AlphabetScrollBar.OnTouchBarListener {

		@Override
		public void onTouch(String letter) {
			
			//������ĸ��ʱ,����ϵ���б���µ�����ĸ���ֵ�λ��
	        for (int i = 0;   i < persons.size(); i++) {  
	            if (persons.get(i).PY.substring(0, 1).compareToIgnoreCase(letter) == 0) { 
	            	m_contactslist.setSelection(i);
	            	break;
	            }  
	        } 
		}
	}
	
    public void getContacts() {
            ContentResolver contentResolver = getContentResolver();
            // ���������ϵ�����ݼ����α�
            Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI ,null, null, null, null);
            // ѭ������
            if (cursor.moveToFirst()) {
                    
                    int idColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
                    int displayNameColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    int NumberColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                    while (cursor.moveToNext()){
                    		Persons person = new Persons();
                            // �����ϵ�˵�ID��
                            String contactId = cursor.getString(idColumn);

                            // �����ϵ������
                            person.Name = cursor.getString(displayNameColumn);
                            person.PY = PinyinUtils.getPingYin(person.Name);
                            person.FisrtSpell = PinyinUtils.getFirstSpell(person.Name);
                            person.Number = cursor.getString(NumberColumn);
//                            Log.v("huahua", "����:"+person.Name + "����:"+person.Number + "��������ĸ:"+person.FisrtSpell );

                            persons.add(person);
                    }
                    cursor.close();
            }
    }
    
	private void filterContacts(String filterStr){
		ArrayList<Persons> filterpersons = new ArrayList<Persons>();
		
        //����������ϵ������,ɸѡ�������ؼ��ֵ���ϵ��
        for (int i = 0; i < persons.size(); i++) {  
            //���˵�����
              if (isStrInString(persons.get(i).Number,filterStr)
            		||isStrInString(persons.get(i).PY,filterStr)
            		||persons.get(i).Name.contains(filterStr)
            		||isStrInString(persons.get(i).FisrtSpell,filterStr)){
                //��ɸѡ��������ϵ��������ӵ�filterpersons������
            	Persons filterperson = new Persons();
            	filterperson.Name = persons.get(i).Name;
            	filterperson.PY = persons.get(i).PY;
            	filterperson.Number = persons.get(i).Number;
            	filterperson.FisrtSpell = persons.get(i).FisrtSpell;
            	filterpersons.add(filterperson);
            }  
        }  
        
        //���û��ƥ�����ϵ��
		if(filterpersons.isEmpty())
		{
			m_contactslist.setEmptyView(m_listEmptyText);
		}
        
        //���б����Ϊ���˵���ϵ��
        m_listadapter.updateListView(filterpersons);
	}
	
	public boolean isStrInString(String bigStr,String smallStr){
		  if(bigStr.toUpperCase().indexOf(smallStr.toUpperCase())>-1){
			  return true;
		  }else{
			  return false;
		  }
	}

}

package com.parse.widget;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.parse.model.FontConverter;
import com.parse.utils.StoreUtil;


public class CustomEditText extends EditText{
	public CustomEditText(Context context){
		super(context);
		if(!isInEditMode()){
			String selected_font = StoreUtil.getInstance().selectFrom("fonts");
			if(selected_font != null){
				if(selected_font.equals("default")){

				}else if( selected_font.equals("zawgyione")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/zawgyione.ttf"));
				}else if( selected_font.equals("ayarwagaung")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/ayarwagaung.ttf"));
				}else if(selected_font.equals("robotocondensedlight")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/robotocondensedlight.ttf"));
				}
			}else{
				setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/robotocondensedlight.ttf"));
			}
		}

	}
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(!isInEditMode()){
			String selected_font = StoreUtil.getInstance().selectFrom("fonts");
			if(selected_font != null){
				if(selected_font.equals("default")){
					
				}else if( selected_font.equals("zawgyione")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/zawgyi.ttf"));
				}else if( selected_font.equals("myanmar3")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/mm3-multi-os.ttf"));
				}else if( selected_font.equals("english")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/roboto-medium.ttf"));
				}
			}else{
				setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/roboto-medium.ttf"));
			}
		}
	}
	
	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		String selected_font = StoreUtil.getInstance().selectFrom("fonts");
		if(selected_font != null){
			if(selected_font.equals("myanmar3")){
				if(text != null){
					text = FontConverter.zg12uni51(text.toString());
				}
			}
		}
		super.setText(text, type);
	}
	
	

}

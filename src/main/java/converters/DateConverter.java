package converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.sql.Date;

@FacesConverter("converter.date")
public class DateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		Date date = null;
		try{
			arg2 = rotateDate(arg2, true);
			date = Date.valueOf(arg2);
		}
		catch(IllegalArgumentException|NullPointerException e){
			FacesContext.getCurrentInstance().addMessage("Ошибка преобразования даты!",new FacesMessage("Ошибка преобразования даты!!"));
		}
		return date;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		String date = arg2.toString();
		date = rotateDate(date, false);
		return date;
	}

	public String rotateDate(String date, boolean fromClient) {
        String[] dateBeforeConvert = (fromClient)?date.split("\\."):date.split("-");
		date = "";
		for (int i = dateBeforeConvert.length-1; i >= 0; i--){
            if (fromClient)
                date = date + dateBeforeConvert[i] + ((i != 0) ? "-" : "");
            else
                date = date + dateBeforeConvert[i] + ((i != 0) ? "." : "");
		}
		return date;
	}
}

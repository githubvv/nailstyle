package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name = "error")
@SessionScoped
public class ErrorBean implements Serializable {
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        if (code.equals("expired"))
            message = "Ваша сессия истекла";
        else if (code.equals("unknown"))
            message = "Неизвестная ошибка!";
        else if (code.equals("400"))
            message = "Ошибка 400. Cтраница не найдена!";
        else if (code.equals("500"))
            message = "Ошибка 500. Cтраница не найдена!";
        return message;
    }
}

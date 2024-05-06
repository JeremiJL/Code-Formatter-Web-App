package jeremi.codeformatter.Controller;

import com.google.googlejavaformat.java.FormatterException;
import jeremi.codeformatter.Model.Expiration;
import jeremi.codeformatter.Model.SnippetDTO;
import jeremi.codeformatter.Model.SnippetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
public class FormatterController {

    private SnippetService snippetService;

    public FormatterController(SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    @GetMapping("/")
    public String getHome(){
        return "home";
    }

    @GetMapping("/format")
    public String getFormat(Model model){

        if (snippetService.getDraft() == null)
            model.addAttribute("snippet", new SnippetDTO());
        else
            model.addAttribute("snippet", snippetService.getDraft());

        return "format";
    }

    @PostMapping("/format")
    public RedirectView postFormat(@RequestParam(name = "action") String action, SnippetDTO snippet){

        if (action.equals("format")) {
            try {
                snippetService.format(snippet);
                snippetService.setDraft(snippet);
                return new RedirectView("/format",true,true);
            } catch (FormatterException e) {
                // Customized error page 1
                return new RedirectView("/error",true,true);
            }
        } else if (action.equals("clear")) {
            snippetService.setDraft(null);
            return new RedirectView("/format",true,true);
        } else {
            return new RedirectView("/save",true,true);
        }
    }

    @GetMapping("/save")
    public String getSave(Model model){
        model.addAttribute(new Expiration());
        return "save";
    }

    @PostMapping("/save")
    public RedirectView postSave(Expiration expiration, @RequestParam(name = "snippetId") String id){

        try {
            if (snippetService.store(id,expiration))
                return new RedirectView("search?snippetId="+id);
            else
                // Customized error page 2
                return new RedirectView("error",true,true);

        } catch (IOException e) {
            // Customized error page 3
            return new RedirectView("error",true,true);
        }
    }

    @GetMapping("/search")
    public String getSearch(Model model, @RequestParam(required = false, name = "snippetId") String id) {
        if (id != null) {
            try {
                model.addAttribute("snippet", snippetService.retrieve(id));
                model.addAttribute("snippetId", id);
            } catch (IOException | ClassNotFoundException e) {
                // This shouldn't be invoked
                return "error";
            }
        }
        return "search";
    }

    @PostMapping("/search")
    public RedirectView postSearch(@RequestParam(required = false, name = "snippetId") String id) {

        try {
            if (snippetService.isPresent(id))
                return new RedirectView("search?snippetId="+id,true,true);
            else
                // Customized error page 4
                return new RedirectView("error",true,true);
        } catch (IOException e) {
            // Customized error page 5
            return new RedirectView("error",true,true);
        }

    }

}

package com.system.shoecleaningsystem.user;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.system.shoecleaningsystem.booking.Booking;
import com.system.shoecleaningsystem.booking.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/index")
    public String goToHome(){
        return "index";
    }

    @GetMapping("/register")
    public String goToRegister() {
        return "register_page";
    }

    @PostMapping("/add/user")
    public String registerUser(@RequestParam("name") String name,
                               @RequestParam("surname") String surname,
                               @RequestParam("username") String username,
                               @RequestParam("password") String password) {

        User user = new User(name, surname, username, password);
        userService.saveUser(user);
        return "index";
    }

    @GetMapping("/login")
    public String goToLogin() {
        return "login_page";
    }

    @PostMapping("/login/user")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(true);
        User user = userService.getByUsernameAndPassword(username, password);

        if (user != null) {
            model.addAttribute("user", user);
            session.setAttribute("user", user);
            return "redirect:/booking_page";
        } else {
            model.addAttribute("loginError", true);
            return "login_page";
        }
    }

    @GetMapping("/booking_page")
    public String goToBooking(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "booking_page";
    }

    @PostMapping("/make/booking")
    public void createBooking(@RequestParam("shoeName") String shoeName,
                              @RequestParam("shoeSize") Integer shoeSize,
                              @RequestParam("shoeColour") String shoeColour,
                              @RequestParam("paymentMethod") String paymentMethod,
                              Model model, HttpServletRequest request,
                              HttpServletResponse response,
                              RedirectAttributes redirectAttributes) throws IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("/login");
            return;
        }

        Booking booking = new Booking(shoeName, shoeSize, shoeColour, paymentMethod, user);
        bookingService.makeBooking(booking);

        session.setAttribute("user", user);
        model.addAttribute("user", user);

        // Generate PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Booking Confirmation"));
        document.add(new Paragraph("User: " + user.getName() + " " + user.getSurname()));
        document.add(new Paragraph("Shoe Name: " + shoeName));
        document.add(new Paragraph("Shoe Size: " + shoeSize));
        document.add(new Paragraph("Shoe Colour: " + shoeColour));
        document.add(new Paragraph("Payment Method: " + paymentMethod));
        document.close();

        // Set response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=booking_confirmation.pdf");
        response.setContentLength(baos.size());

        // Write PDF to response
        response.getOutputStream().write(baos.toByteArray());
        response.getOutputStream().flush();

        redirectAttributes.addFlashAttribute("message", "Booking Successful!");
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidate session
        session.invalidate();
        return "redirect:/index";
    }
}

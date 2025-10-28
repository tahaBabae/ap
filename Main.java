import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Student> students = new ArrayList<>();
    private static List<Employee> employees = new ArrayList<>();
    private static List<Book> books = new ArrayList<>();
    private static List<Loan> loans = new ArrayList<>();
    private static User currentUser = null;

    public static void main(String[] args) {
        initializeData();
        showMainMenu();
    }

    private static void initializeData() {
        // ایجاد مدیر پیش‌فرض
        employees.add(new Employee("admin", "admin123", "Manager"));

        // ایجاد داده‌های نمونه
        students.add(new Student("student1", "pass123", "John Doe", true));
        students.add(new Student("student2", "pass456", "Jane Smith", true));

        books.add(new Book("Introduction to Java", "John Smith", 2020, "1234567890"));
        books.add(new Book("Data Structures", "Alice Johnson", 2019, "0987654321"));
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== سیستم مدیریت کتابخانه دانشگاه ===");
            System.out.println("1. مهمان");
            System.out.println("2. دانشجو");
            System.out.println("3. کارمند کتابخانه");
            System.out.println("4. مدیر سیستم");
            System.out.println("0. خروج");
            System.out.print("انتخاب کنید: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showGuestMenu();
                    break;
                case 2:
                    showStudentMenu();
                    break;
                case 3:
                    showEmployeeMenu();
                    break;
                case 4:
                    showManagerMenu();
                    break;
                case 0:
                    System.out.println("خروج از سیستم...");
                    return;
                default:
                    System.out.println("انتخاب نامعتبر!");
            }
        }
    }

    // کلاس‌های مدل
    static abstract class User {
        protected String username;
        protected String password;
        protected String role;

        public User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public boolean authenticate(String username, String password) {
            return this.username.equals(username) && this.password.equals(password);
        }
    }

    static class Student extends User {
        private String fullName;
        private boolean active;

        public Student(String username, String password, String fullName, boolean active) {
            super(username, password, "Student");
            this.fullName = fullName;
            this.active = active;
        }

        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        public String getFullName() { return fullName; }
    }

    static class Employee extends User {
        public Employee(String username, String password, String role) {
            super(username, password, role);
        }
    }

    static class Book {
        private String title;
        private String author;
        private int publicationYear;
        private String isbn;
        private boolean available;

        public Book(String title, String author, int publicationYear, String isbn) {
            this.title = title;
            this.author = author;
            this.publicationYear = publicationYear;
            this.isbn = isbn;
            this.available = true;
        }

        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public int getPublicationYear() { return publicationYear; }
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
    }

    static class Loan {
        private String studentUsername;
        private String bookIsbn;
        private LocalDate startDate;
        private LocalDate endDate;
        private String employeeUsername;
        private boolean approved;
        private boolean returned;
        private LocalDate actualReturnDate;

        public Loan(String studentUsername, String bookIsbn, LocalDate startDate,
                    LocalDate endDate, String employeeUsername) {
            this.studentUsername = studentUsername;
            this.bookIsbn = bookIsbn;
            this.startDate = startDate;
            this.endDate = endDate;
            this.employeeUsername = employeeUsername;
            this.approved = false;
            this.returned = false;
        }

        public String getStudentUsername() { return studentUsername; }
        public String getBookIsbn() { return bookIsbn; }
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() { return endDate; }
        public boolean isApproved() { return approved; }
        public void setApproved(boolean approved) { this.approved = approved; }
        public boolean isReturned() { return returned; }
        public void setReturned(boolean returned) { this.returned = returned; }
        public void setActualReturnDate(LocalDate date) { this.actualReturnDate = date; }

        public LocalDate getActualReturnDate() {
            return actualReturnDate;
        }

        public Object getEmployeeUsername() {
            return null;
        }
    }

    // منوی مهمان
    private static void showGuestMenu() {
        while (true) {
            System.out.println("\n=== منوی مهمان ===");
            System.out.println("1. مشاهده تعداد دانشجویان ثبت‌نام کرده");
            System.out.println("2. جستجوی کتاب بر اساس نام");
            System.out.println("3. مشاهده اطلاعات آماری");
            System.out.println("0. بازگشت به منوی اصلی");
            System.out.print("انتخاب کنید: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("تعداد دانشجویان ثبت‌نام کرده: " + students.size());
                    break;
                case 2:
                    searchBooksForGuest();
                    break;
                case 3:
                    showGuestStatistics();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("انتخاب نامعتبر!");
            }
        }
    }

    private static void searchBooksForGuest() {
        System.out.print("نام کتاب برای جستجو: ");
        String title = scanner.nextLine();

        List<Book> results = books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nنتایج جستجو:");
        for (Book book : results) {
            System.out.println("عنوان: " + book.getTitle() +
                    " | نویسنده: " + book.getAuthor() +
                    " | سال نشر: " + book.getPublicationYear());
        }
    }

    private static void showGuestStatistics() {
        long activeLoans = loans.stream()
                .filter(loan -> loan.isApproved() && !loan.isReturned())
                .count();

        System.out.println("آمار کلی سیستم:");
        System.out.println("تعداد دانشجویان: " + students.size());
        System.out.println("تعداد کتاب‌ها: " + books.size());
        System.out.println("تعداد کل امانت‌ها: " + loans.size());
        System.out.println("کتاب‌های در حال امانت: " + activeLoans);
    }

    // منوی دانشجو
    private static void showStudentMenu() {
        while (true) {
            System.out.println("\n=== منوی دانشجو ===");
            System.out.println("1. ثبت‌نام");
            System.out.println("2. ورود");
            System.out.println("0. بازگشت به منوی اصلی");
            System.out.print("انتخاب کنید: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerStudent();
                    break;
                case 2:
                    if (loginStudent()) {
                        showLoggedInStudentMenu();
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("انتخاب نامعتبر!");
            }
        }
    }

    private static void registerStudent() {
        System.out.print("نام کاربری: ");
        String username = scanner.nextLine();
        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();
        System.out.print("نام کامل: ");
        String fullName = scanner.nextLine();

        students.add(new Student(username, password, fullName, true));
        System.out.println("ثبت‌نام با موفقیت انجام شد!");
    }

    private static boolean loginStudent() {
        System.out.print("نام کاربری: ");
        String username = scanner.nextLine();
        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();

        for (Student student : students) {
            if (student.authenticate(username, password) && student.isActive()) {
                currentUser = student;
                System.out.println("ورود موفق!");
                return true;
            }
        }
        System.out.println("ورود ناموفق!");
        return false;
    }

    private static void showLoggedInStudentMenu() {
        while (true) {
            System.out.println("\n=== منوی دانشجوی وارد شده ===");
            System.out.println("1. جستجوی کتاب");
            System.out.println("2. ثبت درخواست امانت");
            System.out.println("0. خروج");
            System.out.print("انتخاب کنید: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    searchBooksForStudent();
                    break;
                case 2:
                    requestLoan();
                    break;
                case 0:
                    currentUser = null;
                    return;
                default:
                    System.out.println("انتخاب نامعتبر!");
            }
        }
    }

    private static void searchBooksForStudent() {
        System.out.print("عنوان کتاب (اختیاری): ");
        String title = scanner.nextLine();
        System.out.print("نام نویسنده (اختیاری): ");
        String author = scanner.nextLine();
        System.out.print("سال نشر (0 برای نادیده گرفتن): ");
        int year = scanner.nextInt();
        scanner.nextLine();

        List<Book> results = books.stream()
                .filter(book -> title.isEmpty() || book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(book -> author.isEmpty() || book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .filter(book -> year == 0 || book.getPublicationYear() == year)
                .collect(Collectors.toList());

        System.out.println("\nنتایج جستجو:");
        for (Book book : results) {
            String status = book.isAvailable() ? "موجود" : "امانت داده شده";
            System.out.println("عنوان: " + book.getTitle() +
                    " | نویسنده: " + book.getAuthor() +
                    " | سال: " + book.getPublicationYear() +
                    " | وضعیت: " + status);
        }
    }

    private static void requestLoan() {
        System.out.print("ISBN کتاب: ");
        String isbn = scanner.nextLine();
        System.out.print("تاریخ شروع (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine();
        System.out.print("تاریخ پایان (YYYY-MM-DD): ");
        String endDateStr = scanner.nextLine();

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        loans.add(new Loan(currentUser.username, isbn, startDate, endDate, null));
        System.out.println("درخواست امانت ثبت شد!");
    }

    // منوی کارمند
    private static void showEmployeeMenu() {
        while (true) {
            System.out.println("\n=== منوی کارمند ===");
            System.out.println("1. ورود");
            System.out.println("0. بازگشت به منوی اصلی");
            System.out.print("انتخاب کنید: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    if (loginEmployee()) {
                        showLoggedInEmployeeMenu();
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("انتخاب نامعتبر!");
            }
        }
    }

    private static boolean loginEmployee() {
        System.out.print("نام کاربری: ");
        String username = scanner.nextLine();
        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();

        for (Employee employee : employees) {
            if (employee.authenticate(username, password)) {
                currentUser = employee;
                System.out.println("ورود موفق!");
                return true;
            }
        }
        System.out.println("ورود ناموفق!");
        return false;
    }

    private static void showLoggedInEmployeeMenu() {
        while (true) {
            System.out.println("\n=== منوی کارمند وارد شده ===");
            System.out.println("1. تغییر رمز عبور");
            System.out.println("2. ثبت کتاب جدید");
            System.out.println("3. جستجو و ویرایش کتاب");
            System.out.println("4. تایید درخواست‌های امانت");
            System.out.println("5. تاریخچه امانت دانشجو");
            System.out.println("6. فعال/غیرفعال کردن دانشجو");
            System.out.println("7. ثبت دریافت کتاب");
            System.out.println("0. خروج");
            System.out.print("انتخاب کنید: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    changePassword();
                    break;
                case 2:
                    addBook();
                    break;
                case 3:
                    searchAndEditBook();
                    break;
                case 4:
                    approveLoans();
                    break;
                case 5:
                    showStudentLoanHistory();
                    break;
                case 6:
                    toggleStudentStatus();
                    break;
                case 7:
                    registerBookReturn();
                    break;
                case 0:
                    currentUser = null;
                    return;
                default:
                    System.out.println("انتخاب نامعتبر!");
            }
        }
    }

    private static void changePassword() {
        System.out.print("رمز عبور جدید: ");
        String newPassword = scanner.nextLine();
        currentUser.password = newPassword;
        System.out.println("رمز عبور با موفقیت تغییر یافت!");
    }

    private static void addBook() {
        System.out.print("عنوان کتاب: ");
        String title = scanner.nextLine();
        System.out.print("نام نویسنده: ");
        String author = scanner.nextLine();
        System.out.print("سال نشر: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        books.add(new Book(title, author, year, isbn));
        System.out.println("کتاب با موفقیت ثبت شد!");
    }

    private static void searchAndEditBook() {
        System.out.print("ISBN کتاب برای جستجو: ");
        String isbn = scanner.nextLine();

        Optional<Book> bookOpt = books.stream()
                .filter(book -> book.getTitle().equals(isbn))
                .findFirst();

        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            System.out.println("کتاب یافت شد: " + book.getTitle());
            // ویرایش اطلاعات کتاب می‌تواند اینجا اضافه شود
        } else {
            System.out.println("کتاب یافت نشد!");
        }
    }

    private static void approveLoans() {
        LocalDate today = LocalDate.now();
        List<Loan> pendingLoans = loans.stream()
                .filter(loan -> !loan.isApproved())
                .filter(loan -> loan.getStartDate().isBefore(today.plusDays(1)))
                .collect(Collectors.toList());

        System.out.println("درخواست‌های pending:");
        for (int i = 0; i < pendingLoans.size(); i++) {
            Loan loan = pendingLoans.get(i);
            System.out.println((i+1) + ". دانشجو: " + loan.getStudentUsername() +
                    " | کتاب: " + loan.getBookIsbn());
        }

        System.out.print("شماره درخواست برای تایید (0 برای بازگشت): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= pendingLoans.size()) {
            pendingLoans.get(choice-1).setApproved(true);
            System.out.println("درخواست تایید شد!");
        }
    }

    private static void showStudentLoanHistory() {
        System.out.print("نام کاربری دانشجو: ");
        String username = scanner.nextLine();

        List<Loan> studentLoans = loans.stream()
                .filter(loan -> loan.getStudentUsername().equals(username))
                .collect(Collectors.toList());

        long totalLoans = studentLoans.size();
        long notReturned = studentLoans.stream().filter(loan -> !loan.isReturned()).count();
        long delayedReturns = studentLoans.stream()
                .filter(loan -> loan.isReturned() && loan.getActualReturnDate().isAfter(loan.getEndDate()))
                .count();

        System.out.println("تاریخچه امانت دانشجو:");
        System.out.println("تعداد کل امانت‌ها: " + totalLoans);
        System.out.println("کتاب‌های تحویل داده نشده: " + notReturned);
        System.out.println("امانت‌های با تاخیر: " + delayedReturns);
    }

    private static void toggleStudentStatus() {
        System.out.print("نام کاربری دانشجو: ");
        String username = scanner.nextLine();

        Optional<Student> studentOpt = students.stream()
                .filter(s -> s.username.equals(username))
                .findFirst();

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setActive(!student.isActive());
            String status = student.isActive() ? "فعال" : "غیرفعال";
            System.out.println("وضعیت دانشجو به " + status + " تغییر یافت!");
        } else {
            System.out.println("دانشجو یافت نشد!");
        }
    }

    private static void registerBookReturn() {
        System.out.print("ISBN کتاب: ");
        String isbn = scanner.nextLine();
        System.out.print("نام کاربری دانشجو: ");
        String username = scanner.nextLine();

        Optional<Loan> loanOpt = loans.stream()
                .filter(loan -> loan.getBookIsbn().equals(isbn) &&
                        loan.getStudentUsername().equals(username) &&
                        loan.isApproved() && !loan.isReturned())
                .findFirst();

        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            loan.setReturned(true);
            loan.setActualReturnDate(LocalDate.now());

            Optional<Book> bookOpt = books.stream()
                    .filter(book -> book.getTitle().equals(isbn))
                    .findFirst();
            bookOpt.ifPresent(book -> book.setAvailable(true));

            System.out.println("دریافت کتاب ثبت شد!");
        } else {
            System.out.println("امانت مربوطه یافت نشد!");
        }
    }

    // منوی مدیر
    private static void showManagerMenu() {
        while (true) {
            System.out.println("\n=== منوی مدیر ===");
            System.out.println("1. ورود");
            System.out.println("0. بازگشت به منوی اصلی");
            System.out.print("انتخاب کنید: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    if (loginManager()) {
                        showLoggedInManagerMenu();
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("انتخاب نامعتبر!");
            }
        }
    }

    private static boolean loginManager() {
        System.out.print("نام کاربری: ");
        String username = scanner.nextLine();
        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();

        Optional<Employee> manager = employees.stream()
                .filter(emp -> emp.role.equals("Manager") && emp.authenticate(username, password))
                .findFirst();

        if (manager.isPresent()) {
            currentUser = manager.get();
            System.out.println("ورود مدیر موفق!");
            return true;
        }
        System.out.println("ورود ناموفق!");
        return false;
    }

    private static void showLoggedInManagerMenu() {
        while (true) {
            System.out.println("\n=== منوی مدیر وارد شده ===");
            System.out.println("1. تعریف کارمند جدید");
            System.out.println("2. مشاهده عملکرد کارمندان");
            System.out.println("3. آمار امانت کتاب‌ها");
            System.out.println("4. آمار دانشجویان");
            System.out.println("0. خروج");
            System.out.print("انتخاب کنید: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    showEmployeePerformance();
                    break;
                case 3:
                    showLoanStatistics();
                    break;
                case 4:
                    showStudentStatistics();
                    break;
                case 0:
                    currentUser = null;
                    return;
                default:
                    System.out.println("انتخاب نامعتبر!");
            }
        }
    }

    private static void addEmployee() {
        System.out.print("نام کاربری کارمند: ");
        String username = scanner.nextLine();
        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();

        employees.add(new Employee(username, password, "Employee"));
        System.out.println("کارمند جدید با موفقیت اضافه شد!");
    }

    private static void showEmployeePerformance() {
        for (Employee emp : employees) {
            if (emp.role.equals("Employee")) {
                long registeredBooks = books.stream().count(); // ساده‌سازی
                long approvedLoans = loans.stream()
                        .filter(loan -> loan.isApproved() && loan.getEmployeeUsername() != null)
                        .count();
                long returnedBooks = loans.stream()
                        .filter(Loan::isReturned)
                        .count();

                System.out.println("کارمند: " + emp.username);
                System.out.println("  کتاب‌های ثبت شده: " + registeredBooks);
                System.out.println("  امانت‌های داده شده: " + approvedLoans);
                System.out.println("  کتاب‌های تحویل گرفته: " + returnedBooks);
            }
        }
    }

    private static void showLoanStatistics() {
        long totalRequests = loans.size();
        long totalApproved = loans.stream().filter(Loan::isApproved).count();

        double avgDays = loans.stream()
                .filter(Loan::isReturned)
                .mapToLong(loan -> ChronoUnit.DAYS.between(loan.getStartDate(), loan.getActualReturnDate()))
                .average()
                .orElse(0.0);

        System.out.println("آمار امانت کتاب‌ها:");
        System.out.println("تعداد درخواست‌ها: " + totalRequests);
        System.out.println("تعداد امانت‌های داده شده: " + totalApproved);
        System.out.println("میانگین روزهای امانت: " + String.format("%.2f", avgDays));
    }

    private static void showStudentStatistics() {
        Map<String, List<Loan>> loansByStudent = loans.stream()
                .collect(Collectors.groupingBy(Loan::getStudentUsername));

        System.out.println("آمار دانشجویان:");
        for (Map.Entry<String, List<Loan>> entry : loansByStudent.entrySet()) {
            String student = entry.getKey();
            List<Loan> studentLoans = entry.getValue();

            long totalLoans = studentLoans.size();
            long notReturned = studentLoans.stream().filter(loan -> !loan.isReturned()).count();
            long delayed = studentLoans.stream()
                    .filter(loan -> loan.isReturned() && loan.getActualReturnDate().isAfter(loan.getEndDate()))
                    .count();

            System.out.println("دانشجو: " + student);
            System.out.println("  کل امانت‌ها: " + totalLoans);
            System.out.println("  تحویل داده نشده: " + notReturned);
            System.out.println("  با تاخیر: " + delayed);
        }

        // نمایش 10 دانشجوی با بیشترین تاخیر
        System.out.println("\n10 دانشجوی با بیشترین تاخیر:");
        // پیاده‌سازی منطق مرتب‌سازی بر اساس تاخیر
    }
}
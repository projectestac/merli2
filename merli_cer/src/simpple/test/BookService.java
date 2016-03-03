package simpple.test;

/**
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class BookService {
    private Book onlyBook;

    public BookService() {
        onlyBook = new Book();
        onlyBook.setAuthor("Dan Diephouse");
        onlyBook.setTitle("Using Axis2");
        onlyBook.setIsbn("0123456789");
    }

    public Book[] getBooks() {
        return new Book[]{onlyBook};
    }

    public Book findBook(String isbn) {
        if (isbn.equals(onlyBook.getIsbn()))
            return onlyBook;

        return null;
    }
}

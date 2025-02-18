import java.io.*;
import java.util.*;

/**
 * Represents a contact with name, phone number, email, and address.
 */
class Contact implements Serializable {
    String name, phone, email, address;
    boolean isFavorite;

    public Contact(String name, String phone, String email, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.isFavorite = false;  // Default is not favorite
    }

    public void markFavorite() {
        this.isFavorite = !this.isFavorite;
    }

    @Override
    public String toString() {
        return (isFavorite ? "⭐ " : "") + name + " | " + phone + " | " + email + " | " + address;
    }
}

/**
 * AddressBook class manages contact operations: add, view, search, update, delete, and export.
 */
class AddressBook {
    List<Contact> contacts = new ArrayList<>();
    String dataFile = "contacts.txt";

    // Constructor: Load contacts on startup
    public AddressBook() {
        loadContactsFromFile();
    }

    // Add a new contact
    public void addContact(Contact contact) {
        contacts.add(contact);
        saveContactsToFile();
        System.out.println("✅ Contact added successfully!");
    }

    // View all contacts
    public void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("📂 No contacts available.");
            return;
        }
        contacts.forEach(System.out::println);
    }

    // Search contacts by name, phone, or email
    public void searchContacts(String query) {
        boolean found = false;
        for (Contact contact : contacts) {
            if (contact.name.toLowerCase().contains(query.toLowerCase()) ||
                    contact.phone.contains(query) ||
                    contact.email.toLowerCase().contains(query.toLowerCase())) {
                System.out.println(contact);
                found = true;
            }
        }
        if (!found) System.out.println("❌ No matching contacts found.");
    }

    // Update a contact
    public void updateContact(String name) {
        for (Contact contact : contacts) {
            if (contact.name.equalsIgnoreCase(name)) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("New Phone: ");
                contact.phone = scanner.nextLine();
                System.out.print("New Email: ");
                contact.email = scanner.nextLine();
                System.out.print("New Address: ");
                contact.address = scanner.nextLine();
                saveContactsToFile();
                System.out.println("✅ Contact updated!");
                return;
            }
        }
        System.out.println("❌ Contact not found.");
    }

    // Delete a contact
    public void deleteContact(String name) {
        contacts.removeIf(contact -> contact.name.equalsIgnoreCase(name));
        saveContactsToFile();
        System.out.println("✅ Contact deleted.");
    }

    // Sort contacts alphabetically by name
    public void sortContacts() {
        contacts.sort(Comparator.comparing(contact -> contact.name.toLowerCase()));
        saveContactsToFile();
        System.out.println("✅ Contacts sorted by name.");
    }

    // Export contacts to CSV
    public void exportToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("contacts.csv"))) {
            writer.println("Name,Phone,Email,Address");
            for (Contact contact : contacts) {
                writer.println(contact.name + "," + contact.phone + "," + contact.email + "," + contact.address);
            }
            System.out.println("✅ Contacts exported to contacts.csv");
        } catch (IOException e) {
            System.out.println("❌ Error exporting contacts.");
        }
    }

    // Mark a contact as favorite
    public void markAsFavorite(String name) {
        for (Contact contact : contacts) {
            if (contact.name.equalsIgnoreCase(name)) {
                contact.markFavorite();
                saveContactsToFile();
                System.out.println("✅ Favorite status toggled.");
                return;
            }
        }
        System.out.println("❌ Contact not found.");
    }

    // Save contacts to file
    public void saveContactsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(contacts);
        } catch (IOException e) {
            System.out.println("❌ Error saving contacts.");
        }
    }

    // Load contacts from file
    public void loadContactsFromFile() {
        File file = new File(dataFile);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            contacts = (List<Contact>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Error loading contacts.");
        }
    }
}

/**
 * Main class for running the Address Book application.
 */
public class AddressBookApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AddressBook addressBook = new AddressBook();

        while (true) {
            System.out.println("\n📖 ADDRESS BOOK MENU 📖");
            System.out.println("1️⃣ Add Contact");
            System.out.println("2️⃣ View Contacts");
            System.out.println("3️⃣ Search Contact");
            System.out.println("4️⃣ Update Contact");
            System.out.println("5️⃣ Delete Contact");
            System.out.println("6️⃣ Sort Contacts");
            System.out.println("7️⃣ Mark as Favorite");
            System.out.println("8️⃣ Export to CSV");
            System.out.println("9️⃣ Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Phone: ");
                    String phone = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Address: ");
                    String address = scanner.nextLine();
                    addressBook.addContact(new Contact(name, phone, email, address));
                    break;
                case 2:
                    addressBook.viewContacts();
                    break;
                case 3:
                    System.out.print("Enter name, phone, or email to search: ");
                    String searchQuery = scanner.nextLine();
                    addressBook.searchContacts(searchQuery);
                    break;
                case 4:
                    System.out.print("Enter name to update: ");
                    String updateName = scanner.nextLine();
                    addressBook.updateContact(updateName);
                    break;
                case 5:
                    System.out.print("Enter name to delete: ");
                    String deleteName = scanner.nextLine();
                    addressBook.deleteContact(deleteName);
                    break;
                case 6:
                    addressBook.sortContacts();
                    break;
                case 7:
                    System.out.print("Enter name to mark as favorite: ");
                    String favoriteName = scanner.nextLine();
                    addressBook.markAsFavorite(favoriteName);
                    break;
                case 8:
                    addressBook.exportToCSV();
                    break;
                case 9:
                    System.out.println("📖 Exiting Address Book...");
                    scanner.close();
                    return;
                default:
                    System.out.println("❌ Invalid option. Try again.");
            }
        }
    }
}

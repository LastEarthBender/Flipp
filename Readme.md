# Flipp Inventory Management App
This project is an inventory management application designed to help users track and manage their items efficiently. 
It provides features for adding, editing, and deleting items, along with a dashboard for quick insights into inventory status.
## Screenshots
![add_item3.png](ss%2Fadd_item3.png)
![add_item_dark.png](ss%2Fadd_item_dark.png)
![add_item_err.png](ss%2Fadd_item_err.png)
![addItem1.png](ss%2FaddItem1.png)
![addItem2.png](ss%2FaddItem2.png)
![dashboard_1.png](ss%2Fdashboard_1.png)
![dashboard_2.png](ss%2Fdashboard_2.png)
![dashboard_dark1.png](ss%2Fdashboard_dark1.png)
![delete_dark.png](ss%2Fdelete_dark.png)
![delete_item1.png](ss%2Fdelete_item1.png)
![delete_item2.png](ss%2Fdelete_item2.png)
![edit2.png](ss%2Fedit2.png)
![edit3.png](ss%2Fedit3.png)
![edit4.png](ss%2Fedit4.png)
![edit_item.png](ss%2Fedit_item.png)
![home_scren.png](ss%2Fhome_scren.png)
![item_list1.png](ss%2Fitem_list1.png)
![item_list_dark.png](ss%2Fitem_list_dark.png)
![offline.png](ss%2Foffline.png)
![product_detail1.png](ss%2Fproduct_detail1.png)
![product_detail_dark.png](ss%2Fproduct_detail_dark.png)
![report1.png](ss%2Freport1.png)
![reports_dark.png](ss%2Freports_dark.png)
![splash_dark.png](ss%2Fsplash_dark.png)
![splash_light.png](ss%2Fsplash_light.png)


Features
Item Management:

Add Item: Users can add new items with details such as name, description, quantity, price, category, and supplier information.

Edit Item: Existing items can be updated with new information.

Delete Item: Users can remove items from the inventory.

Dashboard:

Inventory Summary: Displays a summary of total items, total value, and other key metrics.

Category Breakdown: Shows a breakdown of items by category.

Reports:

Quantity Trends: Displays quantity trends over time.

Top Categories: Highlights the most common categories in the inventory.

Low Stock Warnings: Alerts users to items with low stock levels.

Architecture
MVVM (Model-View-ViewModel) Pattern: Used for separating concerns between the UI and business logic.

Hilt for Dependency Injection: Simplifies the management of dependencies across the app.

Room Persistence Library: Used for local data storage.

MPAndroidChart: Utilized for displaying visual reports.

Setup
Clone the Repository:

bash
git clone https://github.com/your-repo-url/FlippInventoryApp.git
Open in Android Studio:

Import the project into Android Studio.

Ensure you have the latest Android Studio version and necessary SDKs installed.

Build and Run:

Build the project by clicking on the "Build" button or pressing Shift+F9.

Run the app on an emulator or physical device.

Dependencies
MPAndroidChart: For charting.

Hilt: For dependency injection.

Room: For local data storage.

Kotlin Coroutines: For asynchronous operations.
# Jutai: A Farm Equipment Rental System

## Project Overview

**Jutai** is a farm equipment rental system developed to support small and marginal farmers in India. It provides a platform for farmers to rent or lend agricultural machinery, such as tractors and implements. The app aims to improve access to expensive farming equipment, maximize its utilization, and reduce the financial burden on individual farmers.

## Features

1. Login and registration using OTP verification via Firebase
2. Home screen with a bottom navigation bar for easy access to different modules
3. Equipment listing with photo upload from camera or gallery
4. Rent equipment with time-based booking and real-time availability
5. View equipment location on Google Maps along with route drawing and distance
6. Secure payment processing using Razorpay integration
7. Search and filtering options based on price, category, and availability
8. In-app and system notifications for booking and approval updates
9. FAQ and Help section with dynamic translation using Google ML Kit (supports English, Hindi, and Marathi)
10. Rental history with review and rating submission
11. Card view for browsing equipment listings
12. Auto image slider for recently viewed equipment
13. Current location detection using GPS

## Tools and Technologies

* **IDE**: Android Studio
* **Programming Language**: Java
* **Database**: SQLite
* **Authentication**: Firebase (OTP-based)
* **Payment Gateway**: Razorpay
* **APIs**: Google Maps API, Google ML Kit
* **Notifications**: Android Notification Manager
* **Image Handling**: MediaStore, FileProvider
* **UI Components**: Android Jetpack, RecyclerView, ViewPager2

## System Requirements

### PC Requirements

* OS: Windows 10/11 or macOS
* Android Studio with SDK 33 or higher
* Gradle 7.0 or higher
* Emulator or physical Android device for testing

### Required Permissions

* Internet access
* Camera access
* Storage read/write
* Location services
* Notification access

### Device Requirements

* Android 8.0 (API Level 26) or higher
* Working camera
* Internet connectivity

## Database Design

The application uses SQLite for local data storage. The schema includes the following tables:

1. **users** – Stores user credentials and profile details
2. **equipment** – Contains listings of available equipment
3. **rentals** – Maintains rental booking records
4. **payment** – Stores payment transaction details
5. **reviews** – Contains user-submitted reviews and ratings
6. **notifications** – Manages system-generated alerts
7. **recently\_viewed** – Logs user interaction with equipment

## Testing and Output

The application was thoroughly tested through the following methods:

* **Functional Testing**: Verified all modules such as login, booking, payments, and translation work as intended
* **Integration Testing**: Ensured seamless interaction between Firebase, Razorpay, ML Kit, and SQLite
* **Real Device Testing**: Tested performance on physical Android devices
* **Edge Case Handling**: Handled invalid inputs and prevented app crashes through input validation

The application performed as expected and all key functionalities were validated successfully.

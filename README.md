Jutai: A Farm Equipment Rental System

Project Overview
Jutai is a farm equipment rental system developed to support small and marginal farmers in India. It provides a platform for farmers to rent or lend agricultural machinery, such as tractors and implements. The app aims to improve access to expensive farming equipment, maximize its utilization, and reduce the financial burden on individual farmers.

Features
1) Login and registration using OTP verification via Firebase
2) Home screen with a bottom navigation bar for easy access to different modules
3) Equipment listing with photo upload from camera or gallery
4) Rent equipment with time-based booking and real-time availability
5) View equipment location on Google Maps along with route drawing and distance
6) Secure payment processing using Razorpay integration
7) Search and filtering options based on price, category, and availability
8) In-app and system notifications for booking and approval updates
9) FAQ and Help section with dynamic translation using Google ML Kit (supports English, Hindi, and Marathi)
10) Rental history with review and rating submission
11) Card view for browsing equipment listings
12) Auto image slider for recently viewed equipment
13) Current location detection using GPS

Tools and Technologies
IDE: Android Studio
Programming Language: Java
Database: SQLite
Authentication: Firebase (OTP-based)
Payment Gateway: Razorpay
APIs: Google Maps API, Google ML Kit
Notifications: Android Notification Manager
Image Handling: MediaStore, FileProvider
UI Components: Android Jetpack, RecyclerView, ViewPager2

System Requirements
PC Requirements:
OS: Windows 10/11 or macOS
Android Studio (with SDK 33 or higher)
Gradle 7.0 or higher
Emulator or physical Android device for testing

Required Permissions
1) Internet access
2) Camera access
3) Storage read/write
4) Location services
5) Notification access

Device Requirements
Android 8.0 (API Level 26) or higher
Working camera
Internet connectivity

Database Design
The application uses SQLite for local data storage with the following tables:
1) users – Stores user credentials and profile details
2) equipment – Contains listings of available equipment
3) rentals – Maintains rental booking records
4) payment – Stores payment transaction details
5) reviews – Contains user-submitted reviews and ratings
6) notifications – Manages system-generated alerts
7) recently_viewed – Logs user interaction with equipment

Testing and Output
The application was tested through:
1) Functional testing of all modules
2) Integration testing between different APIs (Firebase, Razorpay, ML Kit)
3) Real device testing for performance validation
4) Edge case handling for invalid inputs and exceptions

The application worked as expected, handling all interactions, payments, notifications, and translations successfully.

# 🌍 TourJoyFX - Travel Management Platform

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-Latest-blue?style=for-the-badge&logo=javafx)
![Maven](https://img.shields.io/badge/Maven-3.6+-red?style=for-the-badge&logo=apache-maven)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

*A modern JavaFX application for seamless travel and tourism management*

[Features](#-features) • [Installation](#-installation) • [Usage](#-usage) • [Contributing](#-contributing)

</div>

---

## Features

### **Accommodation Management**
- Browse and manage accommodations with detailed information
- Image galleries for visual representation
- Advanced filtering and search capabilities

### **Monument Explorer**
- Discover historical monuments and landmarks
- Detailed descriptions with rich media content
- Interactive map integration

### **Professional Guide Services**
- Browse certified tour guides
- View ratings and reviews
- Secure booking system with payment integration

### **Feedback System**
- Leave detailed reviews for guides and accommodations
- Rating system for quality assurance
- Community-driven recommendations

### **User Management**
- Secure authentication with email verification
- User profiles with customization options
- Role-based access control (Admin/Client)

### **Analytics Dashboard**
- Real-time statistics and reports
- Booking analytics with visual charts
- Revenue tracking and insights

### **Modern UI/UX**
- Clean, responsive design
- Multiple theme support
- Intuitive navigation

---

## Architecture

```
TourJoyFX/
├── src/main/java/
│   ├── Controller/          # UI Controllers
│   ├── Entities/           # Data Models
│   ├── Services/           # Business Logic
│   ├── Filter/             # Search & Filter Logic
│   └── utils/              # Utility Classes
├── src/main/resources/
│   ├── *.fxml             # UI Layouts
│   ├── *.css              # Styling
│   └── images/            # Assets
└── pom.xml                # Maven Configuration
```

---

## Quick Start

### Prerequisites
- **Java 17+**
- **Maven 3.6+**
- **JavaFX SDK**

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/mahdi-y/TourJoyFx.git
   cd TourJoyFx
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

### Alternative Run Methods

**Using IDE:**
- Import as Maven project
- Run [`HelloApplication.java`](src/main/java/com/example/tourjoy/HelloApplication.java)

**JAR Execution:**
```bash
mvn clean package
java -jar target/tourjoyfx-1.0.jar
```

---

## Tech Stack

| Technology | Purpose | Version |
|------------|---------|---------|
| **Java** | Core Language | 17+ |
| **JavaFX** | UI Framework | Latest |
| **Maven** | Build Tool | 3.6+ |
| **FXML** | UI Layout | - |
| **CSS3** | Styling | - |
| **MySQL** | Database | 8.0+ |
| **Apache PDFBox** | PDF Generation | Latest |
| **ZXing** | QR Code Generation | Latest |

---

## Key Components

### Controllers
- [`AccomodationController`](src/main/java/Controller/AccomodationController.java) - Accommodation management
- [`GuidesFront`](src/main/java/Controller/GuidesFront.java) - Guide booking interface
- [`PaymentController`](src/main/java/Controller/PaymentController.java) - Payment processing
- [`SubscriptionController`](src/main/java/Controller/SubscriptionController.java) - Subscription management

### Services
- User authentication and session management
- Booking and reservation services
- Payment processing integration
- Email notification system

### UI Features
- Modern CSS styling with multiple themes
- Responsive design patterns
- Custom components and controls
- Smooth animations and transitions

---

## Customization

### Themes
The application supports multiple CSS themes located in:
- [`styles.css`](src/main/resources/styles.css) - Main theme
- [`backoffice.css`](src/main/resources/backoffice.css) - Admin interface
- [`styling.css`](src/main/resources/style/styling.css) - Alternative styling

### Adding Custom Themes
1. Create a new CSS file in `src/main/resources/`
2. Define your custom styles
3. Load the theme in your controller:
   ```java
   scene.getStylesheets().add("your-theme.css");
   ```

---

## Configuration

### Database Setup
1. Install MySQL 8.0+
2. Create database: `tourjoy_db`
3. Update connection settings in your configuration files

### Email Configuration
Configure SMTP settings for email notifications in the application properties.

---

## Contributing

We welcome contributions! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Commit your changes**
   ```bash
   git commit -m 'Add amazing feature'
   ```
4. **Push to the branch**
   ```bash
   git push origin feature/amazing-feature
   ```
5. **Open a Pull Request**

### Code Style
- Follow Java naming conventions
- Add JavaDoc comments for public methods
- Ensure CSS follows BEM methodology
- Test your changes thoroughly

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Contact & Support

- **Issues**: [GitHub Issues](https://github.com/mahdi-y/TourJoyFx/issues)
- **Discussions**: [GitHub Discussions](https://github.com/mahdi-y/TourJoyFx/discussions)
- **Email**: support@tourjoy.com

---

## Acknowledgments

- JavaFX community for excellent documentation
- Open source contributors
- Beta testers and feedback providers

---

<div align="center">

**TourJoyFX** - *Making travel planning joyful!*

[![GitHub stars](https://img.shields.io/github/stars/mahdi-y/TourJoyFx?style=social)](https://github.com/mahdi-y/TourJoyFx/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/mahdi-y/TourJoyFx?style=social)](https://github.com/mahdi-y/TourJoyFx/network)


</div>
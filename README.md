# LabelDoc

A lightweight **Java desktop application** for image labeling, built entirely with **Swing** (no external libraries).  
The software runs on any operating system with a Java Runtime Environment (JRE), without requiring additional dependencies.  

![labelDoc 0.2](https://github.com/khmort/LabelDoc/blob/master/Screenshot.png)

---

## ✨ Features
- Cross-platform support (Windows, Linux, macOS)  
- No external dependencies (pure Swing application)  
- Easy-to-use interface for drawing bounding boxes on images  

---

## 🚀 Planned Features
- **Smart box refinement**:  
  Users can draw rough bounding boxes quickly, and built-in functions will automatically refine them for higher accuracy.  

- **Weak-to-strong training workflow**:  
  - Use pre-trained weak models to auto-generate bounding boxes.  
  - Label dataset semi-automatically.  
  - Retrain the model iteratively to improve detection quality.  

---

## 🛠 Requirements
- Java 8 or higher
- No additional libraries needed

---

## ▶️ How to Run
1. Clone the repository:  
   ```bash
   git clone https://github.com/khmort/labelDoc.git
   cd labelDoc
   ```
2. Compile and run labelDoc:
   ```bash
   mvn compile exec:java -Dexec.mainClass="kh.mort.App"
   ```

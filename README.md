# Campus Placement Management System

A full-stack web application designed to manage the complete university campus placement process in one centralized platform.

The system connects students, companies, and placement administrators and simplifies activities such as job posting, eligibility checking, applications, shortlisting, interviews, and final selection.

---

## 🚀 Project Overview

The Campus Placement Management System is developed specifically for university campus placements.

Instead of managing placement activities through multiple disconnected systems, the application provides a centralized platform where:

- Students can create profiles, upload resumes, view eligible jobs, apply for jobs, and track application status.
- Companies can register, create job opportunities, view applicants, check resumes, and update application status.
- Administrators can manage students, companies, jobs, applications, and placement statistics.

---

## 👥 User Roles

### 🎓 Student

Students can:

- Register and login
- Create and update their profile
- Add department, CGPA, and skills
- Upload PDF resume
- View available job opportunities
- Check job eligibility
- Apply for eligible jobs
- Track application status
- Change password
- View placement progress

### 🏢 Company

Companies can:

- Register and login
- Manage company profile
- Change password
- Create job postings
- Update job postings
- Delete jobs when allowed
- View applicants
- View applicant resumes
- Shortlist students
- Schedule interview status
- Mark students as selected or rejected

### 👨‍💼 Admin / Placement Officer

Administrators can:

- View dashboard statistics
- Manage students
- Manage companies
- Manage job postings
- Manage applications
- View placement statistics
- Monitor shortlisted, interview, selected, and rejected applications

---

## ⭐ Key Features

- 🔐 Role-based authentication
- 🔑 JWT-based authorization
- 🔒 BCrypt password encryption
- 🎓 Student profile management
- 🏢 Company management
- 💼 Job posting and management
- ✅ Automatic eligibility checking
- 📄 PDF resume upload
- 👀 Resume viewing for authorized companies
- 📝 Online job applications
- 📊 Application status tracking
- 📈 Placement statistics
- 🔎 Search and filtering
- 🛡️ Ownership-based access control
- 🌐 REST APIs
- 📱 Responsive and professional user interface

---

## 🧠 Automatic Eligibility Checking

The system automatically checks whether a student is eligible for a particular job.

Eligibility is based on:

- Required CGPA
- Required skills

The system provides:

- CGPA status
- Matched skills
- Missing skills
- Overall eligibility result

This helps students quickly identify suitable opportunities and reduces manual eligibility checking.

---

## 🔄 Application Workflow

```text
Student Registration
        ↓
Student Login
        ↓
Complete Profile
        ↓
Upload Resume
        ↓
View Jobs
        ↓
Check Eligibility
        ↓
Apply for Job
        ↓
Company Reviews Application
        ↓
Shortlisted
        ↓
Interview
        ↓
Selected / Rejected
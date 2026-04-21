-- Most Consulted Doctors
SELECT d.name, COUNT(a.appointment_id) AS total_visits
FROM Doctors d
JOIN Appointments a ON d.doctor_id = a.doctor_id
GROUP BY d.name
ORDER BY total_visits DESC;

-- Total Revenue Per Month
SELECT 
    DATE_FORMAT(treatment_date, '%Y-%m') AS month,
    SUM(cost) AS total_revenue
FROM Treatments
GROUP BY month
ORDER BY month;

-- Most Common Diseases
SELECT diagnosis, COUNT(*) AS frequency
FROM Treatments
GROUP BY diagnosis
ORDER BY frequency DESC;

-- Patient Visit Frequency
SELECT p.name, COUNT(a.appointment_id) AS visit_count
FROM Patients p
JOIN Appointments a ON p.patient_id = a.patient_id
GROUP BY p.name
ORDER BY visit_count DESC;

-- Doctor Performance (Revenue + Patients)
SELECT 
    d.name,
    COUNT(DISTINCT a.patient_id) AS total_patients,
    SUM(t.cost) AS total_revenue
FROM Doctors d
JOIN Appointments a ON d.doctor_id = a.doctor_id
JOIN Treatments t ON a.patient_id = t.patient_id
GROUP BY d.name
ORDER BY total_revenue DESC;


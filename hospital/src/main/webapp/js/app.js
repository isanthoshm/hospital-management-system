// ===================================================
//  HOSPITAL MANAGEMENT SYSTEM — MAIN JS
// ===================================================

const BASE = window.location.pathname.split('/').slice(0, 2).join('/') + '/api';
let currentSection = 'dashboard';

// ── ROUTING ────────────────────────────────────────
function navigate(section) {
  currentSection = section;
  document.querySelectorAll('.nav-item').forEach(el =>
    el.classList.toggle('active', el.dataset.section === section));
  document.querySelectorAll('.page-section').forEach(el =>
    el.style.display = el.id === section ? 'block' : 'none');
  document.querySelector('.topbar-title').textContent =
    section.charAt(0).toUpperCase() + section.slice(1).replace(/-/g,' ');
  loaders[section] && loaders[section]();
}

// ── API HELPERS ─────────────────────────────────────
async function apiFetch(url, method = 'GET', body = null) {
  try {
    const opts = { method, headers: {} };
    if (body) {
      opts.body = body;
      if (body instanceof URLSearchParams) {
        opts.headers['Content-Type'] = 'application/x-www-form-urlencoded';
      }
    }
    const res = await fetch(BASE + url, opts);
    const text = await res.text();
    if (!text || text.trim() === '') return {};
    let json;
    try { json = JSON.parse(text); } catch(e) {
      throw new Error('Server error (HTTP ' + res.status + ')');
    }
    if (!res.ok) throw new Error(json.error || 'HTTP ' + res.status);
    return json;
  } catch(err) {
    if (err.name === 'TypeError') throw new Error('Cannot connect to server');
    throw err;
  }
}

function buildForm(data) {
  const params = new URLSearchParams();
  Object.entries(data).forEach(([k, v]) => {
    if (v !== null && v !== undefined && v !== '') {
      params.append(k, v);
    }
  });
  return params;
}

// ── TOAST ───────────────────────────────────────────
function toast(msg, type = 'success') {
  const tc = document.getElementById('toastContainer');
  const t  = document.createElement('div');
  t.className = `toast ${type}`;
  const icons = { success: '✅', error: '❌', info: 'ℹ️' };
  t.innerHTML = `<span>${icons[type]}</span><span>${msg}</span>`;
  tc.appendChild(t);
  setTimeout(() => t.remove(), 3500);
}

// ── MODAL ───────────────────────────────────────────
function openModal(id) { document.getElementById(id).classList.add('open'); }
function closeModal(id) { document.getElementById(id).classList.remove('open'); }

// Close on overlay click
document.addEventListener('click', e => {
  if (e.target.classList.contains('modal-overlay')) {
    e.target.classList.remove('open');
  }
});

// ── BADGE HELPERS ────────────────────────────────────
function statusBadge(status) {
  const map = {
    'Available':'green','Active':'green','Completed':'green','Paid':'green',
    'Admitted':'blue','Scheduled':'blue','Processing':'blue','Partial':'blue',
    'Outpatient':'purple',
    'On Leave':'amber','Busy':'amber','Pending':'amber','Occupied':'amber',
    'Discharged':'gray','Cancelled':'gray','No Show':'gray','Inactive':'gray',
    'Maintenance':'red',
  };
  const cls = map[status] || 'gray';
  return `<span class="badge badge-${cls}">${status}</span>`;
}

// ── FORMAT DATE ──────────────────────────────────────
function fmtDate(d) { return d ? new Date(d).toLocaleDateString('en-IN') : '—'; }

// ─────────────────────────────────────────────────────
//  DASHBOARD
// ─────────────────────────────────────────────────────
async function loadDashboard() {
  try {
    const data = await apiFetch('/dashboard');
    document.getElementById('stat-patients').textContent   = data.totalPatients ?? 0;
    document.getElementById('stat-doctors').textContent    = data.totalDoctors  ?? 0;
    document.getElementById('stat-appts').textContent      = data.todayAppointments ?? 0;
    document.getElementById('stat-rooms').textContent      = data.availableRooms ?? 0;
    document.getElementById('stat-staff').textContent      = data.activeStaff ?? 0;
    document.getElementById('stat-lowstock').textContent   = data.lowStockMedicines ?? 0;
    document.getElementById('stat-labreports').textContent = data.pendingLabReports ?? 0;
  } catch (e) {
    toast('Could not load dashboard stats', 'error');
  }
}

// ─────────────────────────────────────────────────────
//  PATIENTS
// ─────────────────────────────────────────────────────
async function loadPatients(search = '') {
  const tbody = document.getElementById('patients-tbody');
  tbody.innerHTML = '<tr><td colspan="8"><div class="spinner"></div></td></tr>';
  try {
    const url = search ? `/patients?search=${encodeURIComponent(search)}` : '/patients';
    const list = await apiFetch(url);
    if (!list.length) {
      tbody.innerHTML = '<tr><td colspan="8"><div class="empty-state"><div class="icon">🏥</div><p>No patients found</p></div></td></tr>';
      return;
    }
    tbody.innerHTML = list.map(p => `
      <tr>
        <td><span class="badge badge-blue">#${p.patientId}</span></td>
        <td><strong>${p.fullName}</strong></td>
        <td>${p.gender || '—'}</td>
        <td><span class="badge badge-red">${p.bloodGroup || '—'}</span></td>
        <td>${p.phone || '—'}</td>
        <td>${statusBadge(p.status)}</td>
        <td>${fmtDate(p.createdAt)}</td>
        <td>
          <div style="display:flex;gap:4px">
            <button class="btn btn-secondary btn-sm btn-icon" onclick="editPatient(${p.patientId})">✏️</button>
            <button class="btn btn-danger btn-sm btn-icon" onclick="deletePatient(${p.patientId},'${p.fullName}')">🗑️</button>
          </div>
        </td>
      </tr>`).join('');
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="8" style="color:var(--red);padding:20px">${e.message}</td></tr>`;
  }
}

async function savePatient() {
  const id = document.getElementById('patient-id').value;
  const data = {
    fullName: document.getElementById('p-name').value,
    dob:      document.getElementById('p-dob').value,
    gender:   document.getElementById('p-gender').value,
    bloodGroup: document.getElementById('p-blood').value,
    phone:    document.getElementById('p-phone').value,
    email:    document.getElementById('p-email').value,
    address:  document.getElementById('p-address').value,
    emergencyContact: document.getElementById('p-ec-name').value,
    emergencyPhone:   document.getElementById('p-ec-phone').value,
    status:   document.getElementById('p-status').value,
  };
  if (!data.fullName || !data.phone) { toast('Name and phone are required','error'); return; }
  try {
    const method = id ? 'PUT' : 'POST';
    const url    = id ? `/patients/${id}` : '/patients';
    const result = await apiFetch(url, method, buildForm(data));
    if (result.success) {
      toast(id ? 'Patient updated' : 'Patient added');
      closeModal('patient-modal');
      loadPatients();
    } else { toast('Operation failed','error'); }
  } catch (e) { toast(e.message, 'error'); }
}

async function editPatient(id) {
  try {
    const p = await apiFetch(`/patients/${id}`);
    document.getElementById('patient-id').value  = p.patientId;
    document.getElementById('p-name').value       = p.fullName || '';
    document.getElementById('p-dob').value        = p.dob ? p.dob.substring(0,10) : '';
    document.getElementById('p-gender').value     = p.gender || '';
    document.getElementById('p-blood').value      = p.bloodGroup || '';
    document.getElementById('p-phone').value      = p.phone || '';
    document.getElementById('p-email').value      = p.email || '';
    document.getElementById('p-address').value    = p.address || '';
    document.getElementById('p-ec-name').value    = p.emergencyContact || '';
    document.getElementById('p-ec-phone').value   = p.emergencyPhone || '';
    document.getElementById('p-status').value     = p.status || 'Outpatient';
    document.getElementById('patient-modal-title').textContent = 'Edit Patient';
    openModal('patient-modal');
  } catch (e) { toast(e.message,'error'); }
}

function newPatient() {
  document.getElementById('patient-form').reset();
  document.getElementById('patient-id').value = '';
  document.getElementById('patient-modal-title').textContent = 'Add Patient';
  openModal('patient-modal');
}

async function deletePatient(id, name) {
  if (!confirm(`Delete patient "${name}"? This cannot be undone.`)) return;
  try {
    const r = await apiFetch(`/patients/${id}`, 'DELETE');
    if (r.success) { toast('Patient deleted'); loadPatients(); }
    else toast('Delete failed','error');
  } catch (e) { toast(e.message,'error'); }
}

// ─────────────────────────────────────────────────────
//  DOCTORS
// ─────────────────────────────────────────────────────
async function loadDoctors(search = '') {
  const tbody = document.getElementById('doctors-tbody');
  tbody.innerHTML = '<tr><td colspan="7"><div class="spinner"></div></td></tr>';
  try {
    const list = await apiFetch('/doctors');
    const filtered = search
      ? list.filter(d => d.fullName.toLowerCase().includes(search.toLowerCase())
                      || d.specialization.toLowerCase().includes(search.toLowerCase()))
      : list;
    if (!filtered.length) {
      tbody.innerHTML = '<tr><td colspan="7"><div class="empty-state"><div class="icon">👨‍⚕️</div><p>No doctors found</p></div></td></tr>';
      return;
    }
    tbody.innerHTML = filtered.map(d => `
      <tr>
        <td><span class="badge badge-blue">#${d.doctorId}</span></td>
        <td><strong>${d.fullName}</strong><br><small style="color:var(--slate-400)">${d.qualification||''}</small></td>
        <td>${d.specialization}</td>
        <td>${d.phone || '—'}</td>
        <td>${d.experienceYrs} yrs</td>
        <td>${statusBadge(d.availability)}</td>
        <td>
          <div style="display:flex;gap:4px">
            <button class="btn btn-secondary btn-sm btn-icon" onclick="editDoctor(${d.doctorId})">✏️</button>
            <button class="btn btn-danger btn-sm btn-icon" onclick="deleteDoctor(${d.doctorId},'${d.fullName}')">🗑️</button>
          </div>
        </td>
      </tr>`).join('');
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="7" style="color:var(--red);padding:20px">${e.message}</td></tr>`;
  }
}

// ─────────────────────────────────────────────────────
//  APPOINTMENTS
// ─────────────────────────────────────────────────────
async function loadAppointments() {
  const tbody = document.getElementById('appts-tbody');
  tbody.innerHTML = '<tr><td colspan="7"><div class="spinner"></div></td></tr>';
  try {
    const list = await apiFetch('/appointments');
    if (!list.length) {
      tbody.innerHTML = '<tr><td colspan="7"><div class="empty-state"><div class="icon">📅</div><p>No appointments</p></div></td></tr>';
      return;
    }
    tbody.innerHTML = list.map(a => `
      <tr>
        <td><span class="badge badge-blue">#${a.appointmentId}</span></td>
        <td>${a.patientName || `ID:${a.patientId}`}</td>
        <td>${a.doctorName || `ID:${a.doctorId}`}<br><small style="color:var(--slate-400)">${a.doctorSpecialization||''}</small></td>
        <td>${fmtDate(a.appointmentDate)}</td>
        <td>${a.appointmentTime ? a.appointmentTime.substring(0,5) : '—'}</td>
        <td>${statusBadge(a.status)}</td>
        <td>
          <button class="btn btn-danger btn-sm btn-icon" onclick="deleteAppt(${a.appointmentId})">🗑️</button>
        </td>
      </tr>`).join('');
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="7" style="color:var(--red);padding:20px">${e.message}</td></tr>`;
  }
}

async function deleteAppt(id) {
  if (!confirm('Delete this appointment?')) return;
  try {
    const r = await apiFetch(`/appointments/${id}`, 'DELETE');
    if (r.success) { toast('Appointment deleted'); loadAppointments(); }
  } catch (e) { toast(e.message,'error'); }
}

// ─────────────────────────────────────────────────────
//  ROOMS
// ─────────────────────────────────────────────────────
async function loadRooms() {
  const tbody = document.getElementById('rooms-tbody');
  tbody.innerHTML = '<tr><td colspan="7"><div class="spinner"></div></td></tr>';
  try {
    const list = await apiFetch('/rooms');
    tbody.innerHTML = list.map(r => `
      <tr>
        <td><strong>${r.roomNumber}</strong></td>
        <td>${r.roomType}</td>
        <td>Floor ${r.floor}</td>
        <td>${r.occupied}/${r.capacity}</td>
        <td>₹${r.pricePerDay}/day</td>
        <td>${statusBadge(r.status)}</td>
        <td>
          <button class="btn btn-secondary btn-sm btn-icon" onclick="editRoom(${r.roomId})">✏️</button>
          <button class="btn btn-danger btn-sm btn-icon" onclick="deleteRoom(${r.roomId})">🗑️</button>
        </td>
      </tr>`).join('');
  } catch (e) { toast(e.message,'error'); }
}

// ─────────────────────────────────────────────────────
//  BILLING
// ─────────────────────────────────────────────────────
async function loadBilling() {
  const tbody = document.getElementById('billing-tbody');
  tbody.innerHTML = '<tr><td colspan="7"><div class="spinner"></div></td></tr>';
  try {
    const list = await apiFetch('/bills');
    if (!list.length) {
      tbody.innerHTML = '<tr><td colspan="7"><div class="empty-state"><div class="icon">💳</div><p>No bills found</p></div></td></tr>';
      return;
    }
    tbody.innerHTML = list.map(b => `
      <tr>
        <td><span class="badge badge-blue">#${b.billId}</span></td>
        <td>${b.patientName || `ID:${b.patientId}`}</td>
        <td>${fmtDate(b.billDate)}</td>
        <td>₹${Number(b.totalAmount||0).toFixed(2)}</td>
        <td>₹${Number(b.paidAmount||0).toFixed(2)}</td>
        <td>${statusBadge(b.status)}</td>
        <td>
          <button class="btn btn-danger btn-sm btn-icon" onclick="deleteBill(${b.billId})">🗑️</button>
        </td>
      </tr>`).join('');
  } catch (e) { toast(e.message,'error'); }
}

// ─────────────────────────────────────────────────────
//  PHARMACY
// ─────────────────────────────────────────────────────
async function loadPharmacy() {
  const tbody = document.getElementById('pharmacy-tbody');
  tbody.innerHTML = '<tr><td colspan="7"><div class="spinner"></div></td></tr>';
  try {
    const list = await apiFetch('/medicines');
    tbody.innerHTML = list.map(m => `
      <tr>
        <td>${m.name}</td>
        <td>${m.category || '—'}</td>
        <td>${m.manufacturer || '—'}</td>
        <td>₹${Number(m.unitPrice||0).toFixed(2)}</td>
        <td>
          <span class="badge badge-${m.stockQty <= 20 ? 'red' : m.stockQty <= 50 ? 'amber' : 'green'}">
            ${m.stockQty} units
          </span>
        </td>
        <td>${fmtDate(m.expiryDate)}</td>
        <td>
          <button class="btn btn-secondary btn-sm btn-icon" onclick="editMedicine(${m.medicineId})">✏️</button>
          <button class="btn btn-danger btn-sm btn-icon" onclick="deleteMedicine(${m.medicineId})">🗑️</button>
        </td>
      </tr>`).join('');
  } catch (e) { toast(e.message,'error'); }
}

// ─────────────────────────────────────────────────────
//  STAFF
// ─────────────────────────────────────────────────────
async function loadStaff() {
  const tbody = document.getElementById('staff-tbody');
  tbody.innerHTML = '<tr><td colspan="7"><div class="spinner"></div></td></tr>';
  try {
    const list = await apiFetch('/staff');
    tbody.innerHTML = list.map(s => `
      <tr>
        <td><strong>${s.fullName}</strong></td>
        <td>${s.role}</td>
        <td>${s.department || '—'}</td>
        <td>${s.phone || '—'}</td>
        <td>₹${Number(s.salary||0).toLocaleString('en-IN')}</td>
        <td>${statusBadge(s.status)}</td>
        <td>
          <button class="btn btn-secondary btn-sm btn-icon" onclick="editStaff(${s.staffId})">✏️</button>
          <button class="btn btn-danger btn-sm btn-icon" onclick="deleteStaff(${s.staffId})">🗑️</button>
        </td>
      </tr>`).join('');
  } catch (e) { toast(e.message,'error'); }
}

// ─────────────────────────────────────────────────────
//  LAB REPORTS
// ─────────────────────────────────────────────────────
async function loadLabReports() {
  const tbody = document.getElementById('lab-tbody');
  tbody.innerHTML = '<tr><td colspan="7"><div class="spinner"></div></td></tr>';
  try {
    const list = await apiFetch('/lab-reports');
    tbody.innerHTML = list.map(lr => `
      <tr>
        <td><span class="badge badge-blue">#${lr.reportId}</span></td>
        <td>${lr.patientName || `ID:${lr.patientId}`}</td>
        <td>${lr.testName}</td>
        <td>${lr.doctorName || '—'}</td>
        <td>${fmtDate(lr.testDate)}</td>
        <td>${statusBadge(lr.status)}</td>
        <td>
          <button class="btn btn-secondary btn-sm btn-icon" onclick="viewReport(${lr.reportId})">👁️</button>
          <button class="btn btn-danger btn-sm btn-icon" onclick="deleteReport(${lr.reportId})">🗑️</button>
        </td>
      </tr>`).join('');
  } catch (e) { toast(e.message,'error'); }
}

// ─────────────────────────────────────────────────────
//  LOADER MAP
// ─────────────────────────────────────────────────────
const loaders = {
  dashboard:   loadDashboard,
  patients:    () => loadPatients(),
  doctors:     () => loadDoctors(),
  appointments:loadAppointments,
  rooms:       loadRooms,
  billing:     loadBilling,
  pharmacy:    loadPharmacy,
  staff:       loadStaff,
  'lab-reports': loadLabReports,
};

// ─────────────────────────────────────────────────────
//  INIT
// ─────────────────────────────────────────────────────
window.addEventListener('DOMContentLoaded', () => {
  // Update date in topbar
  document.getElementById('topbar-date').textContent =
    new Date().toLocaleDateString('en-IN', {weekday:'short', year:'numeric', month:'short', day:'numeric'});

  navigate('dashboard');
});

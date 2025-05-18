/**
 * Advanced Dashboard JavaScript
 * Contains interactive functionalities, charts, and animations
 */

// DOM Elements
const body = document.body;
const sidebar = document.querySelector('.sidebar');
const mobileToggle = document.querySelector('.mobile-toggle');
const sidebarLinks = document.querySelectorAll('.sidebar-link');
const statisticsCards = document.querySelectorAll('.stat-card');
const darkModeToggle = document.querySelector('#darkModeToggle');
const chartCanvases = document.querySelectorAll('.chart-canvas');
const dataTables = document.querySelectorAll('.data-table');
const searchInputs = document.querySelectorAll('.search-input');
const notifications = document.querySelector('#notifications');
const quickActions = document.querySelector('#quickActions');

// Initialize Dashboard
document.addEventListener('DOMContentLoaded', function() {
    // Initialize all components
    initTheme();
    initSidebar();
    initCharts();
    initDataTables();
    initSearch();
    initNotifications();
    initStatisticCounters();
    initQuickActions();
    initDateRangePicker();
    initTooltips();
    
    // Add animated background
    addAnimatedBackground();
    
    // Check if admin is authenticated (will be handled properly on server side)
    checkAuthentication();
});

/**
 * Theme Functionality
 */
function initTheme() {
    // Load theme preference from local storage
    const savedTheme = localStorage.getItem('dashboardTheme');
    if (savedTheme) {
        body.setAttribute('data-theme', savedTheme);
        if (darkModeToggle) {
            darkModeToggle.checked = savedTheme === 'dark';
        }
    }
    
    // Dark mode toggle listener
    if (darkModeToggle) {
        darkModeToggle.addEventListener('change', function() {
            const theme = this.checked ? 'dark' : 'light';
            body.setAttribute('data-theme', theme);
            localStorage.setItem('dashboardTheme', theme);
            
            // Reload charts for proper colors
            initCharts();
        });
    }
}

/**
 * Sidebar Functionality
 */
function initSidebar() {
    // Mobile toggle
    if (mobileToggle) {
        mobileToggle.addEventListener('click', () => {
            sidebar.classList.toggle('active');
        });
    }
    
    // Close sidebar when clicking outside on mobile
    document.addEventListener('click', (e) => {
        if (window.innerWidth <= 768 && 
            !sidebar.contains(e.target) && 
            !mobileToggle.contains(e.target) && 
            sidebar.classList.contains('active')) {
            sidebar.classList.remove('active');
        }
    });
    
    // Sidebar link active state
    const currentPath = window.location.pathname;
    sidebarLinks.forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
        }
    });
}

/**
 * Charts Initialization
 */
function initCharts() {
    // Only initialize if Chart.js is loaded
    if (typeof Chart === 'undefined') return;
    
    // Get theme-aware colors
    const theme = body.getAttribute('data-theme') || 'light';
    const colors = getChartColors(theme);
    
    // Sales Chart
    const salesChart = document.getElementById('salesChart');
    if (salesChart) {
        initSalesChart(salesChart, colors);
    }
    
    // Sales Funnel Chart
    const salesFunnel = document.getElementById('salesFunnel');
    if (salesFunnel) {
        initSalesFunnelChart(salesFunnel, colors);
    }
    
    // Lifetime Revenue Chart
    const lifetimeRevenueChart = document.getElementById('lifetimeRevenueChart');
    if (lifetimeRevenueChart) {
        initLifetimeRevenueChart(lifetimeRevenueChart, colors);
    }
    
    // Product Category Chart
    const productCategoryChart = document.getElementById('productCategoryChart');
    if (productCategoryChart) {
        initProductCategoryChart(productCategoryChart, colors);
    }
    
    // Traffic Source Chart
    const trafficSourceChart = document.getElementById('trafficSourceChart');
    if (trafficSourceChart) {
        initTrafficSourceChart(trafficSourceChart, colors);
    }
}

function getChartColors(theme) {
    return {
        primary: theme === 'dark' ? '#1d6e42' : '#123523',
        secondary: theme === 'dark' ? '#4e9c34' : '#3E7B27',
        tertiary: theme === 'dark' ? '#9ebe5d' : '#85A947',
        accent: theme === 'dark' ? '#f5edd9' : '#EFE3C2',
        textColor: theme === 'dark' ? '#e0e0e0' : '#666666',
        gridColor: theme === 'dark' ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0, 0, 0, 0.1)',
        backgroundColors: [
            'rgba(18, 53, 35, 0.8)',
            'rgba(62, 123, 39, 0.8)',
            'rgba(133, 169, 71, 0.8)',
            'rgba(239, 227, 194, 0.8)',
            'rgba(128, 128, 128, 0.8)'
        ],
        transparent: 'rgba(0, 0, 0, 0)'
    };
}

function initSalesChart(canvas, colors) {
    // Clear existing chart if any
    if (canvas.chart) {
        canvas.chart.destroy();
    }
    
    const ctx = canvas.getContext('2d');
    canvas.chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
            datasets: [
                {
                    label: 'Current Year',
                    data: [620, 720, 750, 790, 850, 895],
                    borderColor: colors.secondary,
                    backgroundColor: hexToRgba(colors.secondary, 0.1),
                    tension: 0.4,
                    fill: true,
                    pointBackgroundColor: colors.secondary,
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2,
                    pointRadius: 4,
                    pointHoverRadius: 6
                },
                {
                    label: 'Previous Year',
                    data: [580, 630, 670, 700, 730, 760],
                    borderColor: colors.accent,
                    backgroundColor: hexToRgba(colors.accent, 0.1),
                    tension: 0.4,
                    fill: true,
                    borderDash: [5, 5],
                    pointBackgroundColor: colors.accent,
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2,
                    pointRadius: 4,
                    pointHoverRadius: 6
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                mode: 'index',
                intersect: false,
            },
            plugins: {
                legend: {
                    display: true,
                    position: 'top',
                    labels: {
                        color: colors.textColor,
                        usePointStyle: true,
                        pointStyle: 'circle'
                    }
                },
                tooltip: {
                    enabled: true,
                    backgroundColor: 'rgba(0, 0, 0, 0.7)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    titleFont: {
                        size: 14,
                        weight: 'bold'
                    },
                    bodyFont: {
                        size: 13
                    },
                    padding: 12,
                    boxPadding: 5,
                    usePointStyle: true,
                    callbacks: {
                        label: function(context) {
                            return context.dataset.label + ': $' + context.formattedValue + 'K';
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: colors.gridColor,
                        drawBorder: false
                    },
                    ticks: {
                        color: colors.textColor,
                        callback: function(value) {
                            return '$' + value + 'K';
                        },
                        padding: 10
                    }
                },
                x: {
                    grid: {
                        display: false
                    },
                    ticks: {
                        color: colors.textColor,
                        padding: 10
                    }
                }
            },
            animations: {
                tension: {
                    duration: 1000,
                    easing: 'linear'
                }
            }
        }
    });
}

function initSalesFunnelChart(canvas, colors) {
    // Clear existing chart if any
    if (canvas.chart) {
        canvas.chart.destroy();
    }
    
    const ctx = canvas.getContext('2d');
    canvas.chart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Visitors', 'Product Views', 'Add to Cart', 'Check-Out', 'Complete Order'],
            datasets: [{
                label: 'Conversion Flow',
                data: [256.2, 198.4, 139.2, 9.4, 5.9],
                backgroundColor: [
                    hexToRgba(colors.backgroundColors[0], 0.8),
                    hexToRgba(colors.backgroundColors[1], 0.8),
                    hexToRgba(colors.backgroundColors[2], 0.8),
                    hexToRgba(colors.backgroundColors[3], 0.8),
                    hexToRgba(colors.backgroundColors[4], 0.8)
                ],
                borderColor: [
                    colors.backgroundColors[0],
                    colors.backgroundColors[1],
                    colors.backgroundColors[2],
                    colors.backgroundColors[3],
                    colors.backgroundColors[4]
                ],
                borderWidth: 1,
                borderRadius: 5,
                maxBarThickness: 50
            }]
        },
        options: {
            indexAxis: 'y',
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    enabled: true,
                    backgroundColor: 'rgba(0, 0, 0, 0.7)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    callbacks: {
                        label: function(context) {
                            return context.formattedValue + 'K users';
                        },
                        afterLabel: function(context) {
                            const data = context.chart.data.datasets[0].data;
                            const index = context.dataIndex;
                            if (index > 0) {
                                const dropoff = ((1 - (data[index] / data[index-1])) * 100).toFixed(1);
                                return 'Dropoff: ' + dropoff + '%';
                            }
                            return '';
                        }
                    }
                }
            },
            scales: {
                y: {
                    grid: {
                        display: false
                    },
                    ticks: {
                        color: colors.textColor
                    }
                },
                x: {
                    grid: {
                        color: colors.gridColor,
                        drawBorder: false
                    },
                    ticks: {
                        color: colors.textColor,
                        callback: function(value) {
                            return value + 'K';
                        }
                    }
                }
            },
            animations: {
                y: {
                    duration: 2000,
                    delay: function(context) {
                        return context.dataIndex * 300;
                    }
                }
            }
        }
    });
}

function initLifetimeRevenueChart(canvas, colors) {
    // Clear existing chart if any
    if (canvas.chart) {
        canvas.chart.destroy();
    }
    
    const ctx = canvas.getContext('2d');
    canvas.chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['1st', '2nd', '3rd', '4th', '5th', '6th'],
            datasets: [
                {
                    label: 'Jan, 2022',
                    data: [0, 180, 350, 450, 510, 580],
                    borderColor: colors.primary,
                    tension: 0.4,
                    fill: false,
                    pointBackgroundColor: colors.primary
                },
                {
                    label: 'Feb, 2022',
                    data: [0, 150, 320, 410, 480, 520],
                    borderColor: colors.secondary,
                    tension: 0.4,
                    fill: false,
                    pointBackgroundColor: colors.secondary
                },
                {
                    label: 'Mar, 2022',
                    data: [0, 130, 280, 390, 450, 490],
                    borderColor: colors.tertiary,
                    tension: 0.4,
                    fill: false,
                    pointBackgroundColor: colors.tertiary
                },
                {
                    label: 'Apr, 2022',
                    data: [0, 120, 250, 350, 420, 460],
                    borderColor: colors.accent,
                    tension: 0.4,
                    fill: false,
                    pointBackgroundColor: colors.accent
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                mode: 'index',
                intersect: false,
            },
            plugins: {
                legend: {
                    position: 'top',
                    align: 'end',
                    labels: {
                        boxWidth: 12,
                        usePointStyle: true,
                        pointStyle: 'circle',
                        color: colors.textColor
                    }
                },
                tooltip: {
                    backgroundColor: 'rgba(0, 0, 0, 0.7)',
                    callbacks: {
                        label: function(context) {
                            return context.dataset.label + ': $' + context.formattedValue;
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: colors.gridColor,
                        drawBorder: false
                    },
                    ticks: {
                        color: colors.textColor,
                        callback: function(value) {
                            if (value >= 1000) {
                                return '$' + value/1000 + 'K';
                            }
                            return '$' + value;
                        }
                    },
                    title: {
                        display: true,
                        text: 'Revenue ($)',
                        color: colors.textColor,
                        font: {
                            size: 12,
                            weight: 'normal'
                        },
                        padding: {top: 0, left: 0, right: 0, bottom: 10}
                    }
                },
                x: {
                    grid: {
                        display: false
                    },
                    ticks: {
                        color: colors.textColor
                    },
                    title: {
                        display: true,
                        text: 'Number of month since first purchase',
                        color: colors.textColor,
                        font: {
                            size: 12,
                            weight: 'normal'
                        },
                        padding: {top: 10, left: 0, right: 0, bottom: 0}
                    }
                }
            },
            animations: {
                y: {
                    duration: 2000,
                    easing: 'easeOutQuart'
                }
            }
        }
    });
}

function initProductCategoryChart(canvas, colors) {
    if (!canvas) return;
    
    // Clear existing chart if any
    if (canvas.chart) {
        canvas.chart.destroy();
    }
    
    const ctx = canvas.getContext('2d');
    canvas.chart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Groceries', 'Beverages', 'Personal Care', 'Household', 'Others'],
            datasets: [{
                data: [35, 25, 20, 15, 5],
                backgroundColor: colors.backgroundColors,
                borderWidth: 2,
                borderColor: '#ffffff'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'right',
                    labels: {
                        color: colors.textColor,
                        padding: 20,
                        font: {
                            size: 12
                        },
                        generateLabels: function(chart) {
                            const data = chart.data;
                            if (data.labels.length && data.datasets.length) {
                                return data.labels.map(function(label, i) {
                                    const value = data.datasets[0].data[i];
                                    const backgroundColor = data.datasets[0].backgroundColor[i];
                                    
                                    return {
                                        text: `${label}: ${value}%`,
                                        fillStyle: backgroundColor,
                                        strokeStyle: '#fff',
                                        lineWidth: 2,
                                        fontColor: colors.textColor,
                                        hidden: isNaN(value) || data.datasets[0].data[i] <= 0
                                    };
                                });
                            }
                            return [];
                        }
                    }
                },
                tooltip: {
                    callbacks: {
                        label: function(tooltipItem) {
                            return tooltipItem.label + ': ' + tooltipItem.formattedValue + '%';
                        }
                    }
                }
            },
            cutout: '65%',
            animations: {
                animateRotate: true,
                animateScale: true
            }
        }
    });
}

function initTrafficSourceChart(canvas, colors) {
    if (!canvas) return;
    
    // Clear existing chart if any
    if (canvas.chart) {
        canvas.chart.destroy();
    }
    
    const ctx = canvas.getContext('2d');
    canvas.chart = new Chart(ctx, {
        type: 'polarArea',
        data: {
            labels: ['Organic Search', 'Direct', 'Social Media', 'Referral', 'Email'],
            datasets: [{
                data: [40, 25, 20, 10, 5],
                backgroundColor: colors.backgroundColors,
                borderWidth: 2,
                borderColor: '#ffffff'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'right',
                    labels: {
                        color: colors.textColor,
                        font: {
                            size: 12
                        }
                    }
                },
                tooltip: {
                    callbacks: {
                        label: function(tooltipItem) {
                            return tooltipItem.label + ': ' + tooltipItem.formattedValue + '%';
                        }
                    }
                }
            },
            scales: {
                r: {
                    ticks: {
                        display: false
                    },
                    grid: {
                        color: colors.gridColor
                    },
                    pointLabels: {
                        color: colors.textColor
                    },
                    angleLines: {
                        color: colors.gridColor
                    }
                }
            },
            animation: {
                animateRotate: true,
                animateScale: true
            }
        }
    });
}

/**
 * Data Tables
 */
function initDataTables() {
    if (typeof $.fn.DataTable === 'undefined') return;
    
    dataTables.forEach(table => {
        $(table).DataTable({
            responsive: true,
            language: {
                search: "",
                searchPlaceholder: "Search...",
                lengthMenu: "Show _MENU_ entries per page",
                info: "Showing _START_ to _END_ of _TOTAL_ entries",
                infoEmpty: "Showing 0 to 0 of 0 entries",
                infoFiltered: "(filtered from _MAX_ total entries)",
                zeroRecords: "No matching records found",
                paginate: {
                    first: '<i class="fas fa-angle-double-left"></i>',
                    previous: '<i class="fas fa-angle-left"></i>',
                    next: '<i class="fas fa-angle-right"></i>',
                    last: '<i class="fas fa-angle-double-right"></i>'
                }
            },
            pagingType: "full_numbers",
            ordering: true,
            pageLength: 10,
            lengthMenu: [[5, 10, 25, 50, -1], [5, 10, 25, 50, "All"]],
            dom: '<"top"fl>rt<"bottom"ip>',
            initComplete: function() {
                // Add custom styling to search input
                $('.dataTables_filter input').addClass('form-control');
                $('.dataTables_length select').addClass('form-select');
            }
        });
    });
}

/**
 * Search Functionality
 */
function initSearch() {
    searchInputs.forEach(input => {
        input.addEventListener('input', (e) => {
            const searchTerm = e.target.value.toLowerCase();
            const tableRows = document.querySelectorAll('tbody tr');
            
            tableRows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(searchTerm) ? '' : 'none';
            });
        });
    });
}

/**
 * Notifications
 */
function initNotifications() {
    if (!notifications) return;
    
    notifications.addEventListener('click', function() {
        // Toggle notifications dropdown
        this.querySelector('.dropdown-menu').classList.toggle('show');
    });
    
    // Close when clicking outside
    document.addEventListener('click', function(e) {
        if (notifications && !notifications.contains(e.target)) {
            const dropdown = notifications.querySelector('.dropdown-menu');
            if (dropdown && dropdown.classList.contains('show')) {
                dropdown.classList.remove('show');
            }
        }
    });
}

/**
 * Quick Actions
 */
function initQuickActions() {
    if (!quickActions) return;
    
    quickActions.addEventListener('click', function() {
        // Toggle quick actions dropdown
        this.querySelector('.dropdown-menu').classList.toggle('show');
    });
    
    // Close when clicking outside
    document.addEventListener('click', function(e) {
        if (quickActions && !quickActions.contains(e.target)) {
            const dropdown = quickActions.querySelector('.dropdown-menu');
            if (dropdown && dropdown.classList.contains('show')) {
                dropdown.classList.remove('show');
            }
        }
    });
}

/**
 * Statistics Counters with animation
 */
function initStatisticCounters() {
    statisticsCards.forEach(card => {
        const valueElement = card.querySelector('.stat-value');
        if (!valueElement) return;
        
        const finalValue = parseFloat(valueElement.getAttribute('data-value') || valueElement.textContent);
        const isCurrency = valueElement.getAttribute('data-currency') === 'true';
        const suffix = valueElement.getAttribute('data-suffix') || '';
        
        // Reset to zero
        valueElement.textContent = isCurrency ? '$0' : '0';
        
        // Animate to final value
        animateValue(valueElement, 0, finalValue, 1500, isCurrency, suffix);
    });
}

function animateValue(element, start, end, duration, isCurrency, suffix) {
    const range = end - start;
    const startTime = performance.now();
    
    function updateValue(timestamp) {
        const elapsed = timestamp - startTime;
        const progress = Math.min(elapsed / duration, 1);
        
        // Easing function: easeOutQuart
        const easedProgress = 1 - Math.pow(1 - progress, 4);
        
        let currentValue = start + range * easedProgress;
        
        // Format value
        let formattedValue;
        if (isCurrency) {
            if (currentValue >= 1000) {
                formattedValue = '$' + (currentValue / 1000).toFixed(1) + 'K';
            } else {
                formattedValue = '$' + currentValue.toFixed(0);
            }
        } else {
            if (currentValue >= 1000) {
                formattedValue = (currentValue / 1000).toFixed(1) + 'K';
            } else {
                formattedValue = currentValue.toFixed(0);
            }
        }
        
        element.textContent = formattedValue + suffix;
        
        if (progress < 1) {
            requestAnimationFrame(updateValue);
        }
    }
    
    requestAnimationFrame(updateValue);
}

/**
 * Date Range Picker
 */
function initDateRangePicker() {
    if (typeof $.fn.daterangepicker === 'undefined') return;
    
    const dateRangePicker = $('#dateRangePicker');
    if (dateRangePicker.length) {
        dateRangePicker.daterangepicker({
            startDate: moment().subtract(29, 'days'),
            endDate: moment(),
            ranges: {
                'Today': [moment(), moment()],
                'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                'This Month': [moment().startOf('month'), moment().endOf('month')],
                'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
            },
            alwaysShowCalendars: true,
            opens: 'left'
        }, function(start, end, label) {
            console.log('Date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
        });
    }
}

/**
 * Tooltips
 */
function initTooltips() {
    const tooltipTriggers = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    tooltipTriggers.forEach(el => {
        new bootstrap.Tooltip(el);
    });
}

/**
 * Animated Background
 */
function addAnimatedBackground() {
    const animatedBg = document.createElement('div');
    animatedBg.classList.add('animated-bg');
    document.body.appendChild(animatedBg);
}

/**
 * Authentication Check
 */
function checkAuthentication() {
    // This will be handled by server-side in a real application
    console.log('Authentication check would be handled server-side in a real application');
}

/**
 * Utility Functions
 */
function hexToRgba(hex, alpha) {
    if (!hex) return `rgba(0, 0, 0, ${alpha})`;
    
    let r, g, b;
    if (hex.length === 4) {
        r = parseInt(hex[1] + hex[1], 16);
        g = parseInt(hex[2] + hex[2], 16);
        b = parseInt(hex[3] + hex[3], 16);
    } else {
        r = parseInt(hex.substring(1, 3), 16);
        g = parseInt(hex.substring(3, 5), 16);
        b = parseInt(hex.substring(5, 7), 16);
    }
    
    return `rgba(${r}, ${g}, ${b}, ${alpha})`;
} 
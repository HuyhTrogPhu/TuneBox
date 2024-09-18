document.addEventListener('DOMContentLoaded', () => {
    const navLinks = document.querySelectorAll('.nav-link');

    navLinks.forEach(link => {
      link.addEventListener('click', (event) => {
        navLinks.forEach(nav => nav.classList.remove('active'));
        event.target.classList.add('active');
      });
    });
  });
  document.addEventListener('DOMContentLoaded', function() {
    var createPostBtn = document.getElementById('create-post-btn');
    var postModal = document.getElementById('post-modal');
    var postTextarea = document.getElementById('post-textarea');
    var closeModal = document.getElementById('close-modal');

    if (createPostBtn && postModal && postTextarea && closeModal) {
      createPostBtn.addEventListener('click', function() {
        postModal.style.display = 'flex';
      });

      closeModal.addEventListener('click', function() {
        postModal.style.display = 'none';
      });

    } else {
      console.error('One or more elements not found');
    }
  });

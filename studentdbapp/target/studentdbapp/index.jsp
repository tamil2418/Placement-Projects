<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>JSP Home</title></head>
<link rel="stylesheet" href="css/style.css">
<body>
  <h2>JSP + Servlet Demo</h2>
  <p>This is JSP. Click to hit the servlet:</p>
  <!-- Button with JS navigation -->
  <button type="button" id="btnJs">Go via JS to /hello</button>

  <script>
    document.getElementById('btnJs').addEventListener('click', function () {
      window.location.href = '<%= request.getContextPath() %>/hello';
    });
  </script>

</body>
</html>

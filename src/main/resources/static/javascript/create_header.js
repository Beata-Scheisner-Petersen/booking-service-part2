//Creates Header Area--------------------------------
document.addEventListener("DOMContentLoaded", () => {
  //Create Outer Card
  const card = document.createElement("div");
  card.className = "card w-auto border-0";
  card.id = "header_field";

  const cardBody = document.createElement("div");
  cardBody.className = "card-body";

  const row = document.createElement("div");
  row.className = "row";

  //Logo Column
  const logoLink = document.createElement("a");
  logoLink.href = thymeleafUrls.home;

  const logoColumn = document.createElement("div");
  logoColumn.className = "col-1";

  const logo = document.createElement("img");
  logo.className = "img-fluid";
  logo.style = "min-width:100px; min-height 100px";
  logo.src = "/images/Black_Cat_Retirement_Home_Logo.png";
  logo.alt = "Black Cat Retirement Home Logo";
  logo.style.cursor = "pointer";

  logoLink.appendChild(logo);
  logoColumn.appendChild(logoLink);

  //Spacer column
  const spacerColumn = document.createElement("div");
  spacerColumn.className = "col-10";

  //Menu button column
  const menuColumn = document.createElement("div");
  menuColumn.className = "col-1";

  const menuButton = document.createElement("button");
  menuButton.id = "side_menu_button";
  menuButton.className = "side_menu_button";
  menuButton.textContent = "☰";

  menuColumn.appendChild(menuButton);

  //Builds Header
  row.appendChild(logoColumn);
  row.appendChild(spacerColumn);
  row.appendChild(menuColumn);

  cardBody.appendChild(row);
  card.appendChild(cardBody);

  document.body.prepend(card); //prepend: Places at the very top of body

  //Create Side Menu----------------------------
  const sideMenuItems = [
      { name: "Start Page", url: thymeleafUrls.home },
      { name: "Booking", url: thymeleafUrls.booking },
      { name: "Login", url: thymeleafUrls.login },
      { name: "Register", url: thymeleafUrls.register },
      { name: "My Page", url: thymeleafUrls.mypage },
      { name: "Logout", url: thymeleafUrls.logout}
  ];

  const nav = document.createElement("nav");
  nav.id = "side_menu";
  nav.className = "side_menu";

  //Side Menu Close Button
  const closeBtn = document.createElement("button");
  closeBtn.id = "side_menu_close_btn";
  closeBtn.className = "close_btn";
  closeBtn.textContent = "✕";

  nav.appendChild(closeBtn);

  //Creates Menu List
  const ul = document.createElement("ul");

  sideMenuItems.forEach((item) => {
    const li = document.createElement("li");
    li.innerHTML = `<a class="bottom_links" href="${item.url}">${item.name}</a>`;
    ul.appendChild(li);
  });

  nav.appendChild(ul);

  //Add menu to body
  document.body.appendChild(nav);

  //Event Listeners for Button------------------------------
  menuButton.addEventListener("click", () => {
    nav.classList.add("open");
  });

  closeBtn.addEventListener("click", () => {
    nav.classList.remove("open");
  });
});

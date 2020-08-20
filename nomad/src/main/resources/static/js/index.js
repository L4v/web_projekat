new Vue
({
    el: "#app",
    data:
    {
        active: false,
        selectedDate: null,
        range: new Date(),
        modal_active: false,
    },
    methods:
    {
        toggleNavClass()
        {
            if(this.active == false)
            {
                return "navbar";
            }
            else
            {
                return "navbar-sticky";
            }
        },
        toggleSearchClass()
        {
            if(this.active == false)
            {
                return "";
            }
            else
            {
                return "search-collapse";
            }
        },
        logout: function()
        {
            let jwt = localStorage.jwt;
            if(jwt){
                localStorage.removeItem("jwt");
                alert("You have successfully logged out");
            } else {
                alert("You are not logged in");
            }
        },
        showModal: function()
        {
            this.modal_active = true;
        },
    },
    mounted()
    {
        window.document.onscroll = () =>
        {
            /*
            let navbar = document.getElementById("nav");
            if(window.scrollY > navbar.offsetTop)
            {
                this.active = true;
            }
            else
            {
                this.active = false;
            }
            */
            let searchbar = document.getElementById("searchbar");
            if(window.scrollY > searchbar.offsetTop)
            {
                this.active = true;
            }
            else
            {
                this.active = false;
            }
        };
    }
});


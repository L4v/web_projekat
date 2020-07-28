new Vue
({
    el: "#nav",
    data:
    {
        active: false
    },
    methods:
    {
        toggleNavClass()
        {
            if(this.active == false)
            {
                return 'navbar';
            }
            else
            {
                return 'navbar sticky';
            }
        },
        logout: function()
        {
        	let jwt = localStorage.jwt;
        	if(jwt){
        		localStorage.removeItem('jwt');
        		alert("You have successfully logged out");
        	} else {
        		alert("You are not logged in");
        	}
        }
    },
    mounted()
    {
        window.document.onscroll = () =>
        {
            let navbar = document.getElementById('nav');
            if(window.scrollY > navbar.offsetTop)
            {
                this.active = true;
            }
            else
            {
                this.active = false;
            }
        }
    }
})

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

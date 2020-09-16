<template>
	<div id="container" class="container">
        <h1>Comments</h1>
        <div id="comments">
            <div v-for="comment in comments" class="comment-container">
                <div class="comment-info">
                    <div class="comment-info-field"><b>By:</b> {{comment.comment.guestId}}</div>
                    <div class="comment-info-field"><b>Apartment:</b> {{comment.apartment.location.address.street}} {{comment.apartment.location.address.streetNumber}} {{comment.apartment.location.address.area}}</div>
                    <div class="comment-info-field"><b>Rating:</b> {{comment.comment.rating}}/5</div>
                    <textarea cols="100" rows="15" disabled>{{comment.comment.text}}</textarea>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
	module.exports = {
		data: function()
		{
			return{
				comments: [],
			}
		},
		
		methods:
	    {	
	    	getCommentsPromise: function()
            {
                let jwt = localStorage.jwt;
                if(!jwt)
                {
                    this.$router.go();
                    return;
                }

                return axios.get("rest/admin_view_comments", {headers: {"Authorization": "Bearer " + jwt}})
                    .then(response => response.data);
            }
	    },
		
		mounted()
		{
		    this.getCommentsPromise().then(response => 
            {
                response.forEach(comment => 
                {
                    this.comments.push(
                        {
                            comment: comment,
                            apartment: {},
                        }
                    );
                });

                axios.get("rest/admin_all_apartments", {headers: {"Authorization": "Bearer " + localStorage.jwt}})
                    .then(response =>
                    {
                        response.data.forEach(apartment =>
                        {
                            for(let comment of this.comments)
                            {
                                if(comment.comment.apartmentId === apartment.id)
                                {
                                    comment.apartment = apartment;
                                    console.log("Apartment found");
                                }
                            }
                        });
                        console.log(JSON.stringify(this.comments));
                    });
            });
	    },
	    
	}
</script>

<style>

    #comment-container
    {
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .comment-info
    {
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
    }

    .comment-info-field b
    {
        font-weight: 500;
    }

    .comment-info-text
    {
        color: #000;
    }
</style>
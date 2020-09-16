<template>
	<div class="container">
		<div id="apartmentInfo">
			<h1>Apartment info</h1>
			<br>
			<div class="slide-container">
				<img v-for="image in apartment.images" :src="image" />
			</div>
			<br>
			<div id="apartment">
				<label>Type:  {{apartment.type}}</label>
				<label>Number of rooms:  {{apartment.noRooms}}</label>
				<label>Number of guests:  {{apartment.noGuests}}</label>
				<label>Price:  {{apartment.price}}</label>
				<label>Amenities:</label>
				<select id="amenities" multiple disabled>
		        	<option v-for="amenity in apartment.amenities" :value="amenity">{{amenity.name}}</option>
		        </select><br>
		        <button @click=showComments()>Show comments</button>
		        <table v-if="showComment">
		        		<tr v-for="comment in apartment.comments">
		        		<td><b>{{comment.guestId}}</b></td>
		        		<td>{{comment.text}}<br>Rating: {{comment.rating}}</td>
		        		</tr>
		        </table>
		    </div>
			<div v-if="allowedComment">
				<br>
				<label>Leave comment</label>
				<textarea v-model="commentText" rows="6" cols="30" placeholder="Comment..."></textarea>
				<br>
				<select name="rating" v-model="rating" required>
			    	<option value="" disabled>Rating</option>
			    	<option value="1">1</option>
			    	<option value="2">2</option>
			    	<option value="3">3</option>
			    	<option value="4">4</option>
			    	<option value="5">5</option>
			     </select>
			     <button class="button-primary" @click="addComment()">Comment</button>
			</div>
			{{successMsg}}
			<router-link to="/guest/apartments">Back</router-link>
		</div>
	</div>
</template>

<script>
	module.exports = {
		props: ['apartment'],
		data: function()
		{
			return {
				apartment: {},
				successMsg: "",
				commentText: "",
				rating: "",
				id: "",
				showComment: false,
				allowedComment: false,
			}
		},
		
		mounted()
		{
			axios.post("rest/check_if_has_reservation", this.apartment, {headers:{"Authorization": "Bearer " + localStorage.jwt}})
			        .then(response =>
			            {
			                this.allowedComment = true;
			            })
			        .catch(response => {
			        		this.allowedComment = false;
			        });
		},
		
	    methods:
	    {    
	    	showComments: function()
	    	{
	    		this.showComment = true;
	    	},
	    
			addComment: function()
			{
				if(!this.commentText && !this.rating)
				{
					successMsg = "Comment text and rating must not be empty";
					return;
				} 
				else if(!this.commentText)
				{
					successMsg = "Comment text must not be empty";
					return;
				} 
				else if(!this.rating)
				{
					successMsg = "Rating must not be empty";
					return;
				}
				else
				{
					let comment = 
					{
						id: "",
						text: this.commentText,
						rating: this.rating,
						apartmentId: this.apartment.id,
						guestId: "",
					};
					
					axios.post("rest/guest_add_comment", comment, {headers:{"Authorization": "Bearer " + localStorage.jwt}})
			        .then(response =>
			            {
			                this.successMsg = "Comment successfully added.";
			            })
			        .catch(response => {
			        		this.successMsg = "Failed adding comment";
			        });
			    }
			}
	    },
    }
</script>

<style scoped>
	.slide-container {
	  overflow: auto;
	  white-space: nowrap;
	}
	#amenities
    {
        height: 100%;
        min-height: 200px;
        min-width: 200px;
    }
</style>
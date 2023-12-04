
$(document).ready(function() {
    	$('#form').parsley(); //parsley still not functional
        $("#search-button").click(function(){
            $("#search-result").empty();
            if($("#query-string").val() == ''){
				alert("please enter the search term");
                console.log("please enter the search term");
            }
            else{
                var searchTerm = $("#query-string").val();
                 $.ajax({
                     url: "http://localhost:4502/bin/solr?query-string="+searchTerm,
                     success: function(result){
                     		console.log(result);
                             $("#search-result").append("<h4> Search results: </h4>");
                            $("#search-result").append("<h5>"+result.numFound+" found for the search term "+searchTerm +"</h5>");
                             $.each(result.responseSearchResultBean, function( index, value ) {
                     			console.log(value);
                    			console.log("the index is:" + index);
                                 var link = "<a target='_blank' href='"+value.path+".html"+"'>"+value.title+"</a>";
                                 console.log(link);                                 
                                 $("#search-result").append(link+"<br>");                            
                             });
                     },
                     error: function(errorThrown, exception){
						 console.log(errorThrown);
                         $("#search-result").append("Something went wrong :( ");
                     }
                 });

            }
        });
    });

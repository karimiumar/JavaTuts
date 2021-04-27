#Refer:
https://vladmihalcea.com/the-best-way-to-handle-the-lazyinitializationexception/
#Introduction

The LazyInitializationException is undoubtedly one of the most common exceptions you can get when using Hibernate. This article is going to summarize the best and the worst ways of handling lazy associations.

#Fetching 101

With JPA, not only you can fetch entities from the database, but you can also fetch entity associations as well. For this reason, JPA defines two FetchType strategies:

    EAGER
    LAZY

## The problem with EAGER fetching

EAGER fetching means that associations are always retrieved along with their parent entity. In reality, EAGER fetching is very bad from a performance perspective because it’s very difficult to come up with a global fetch policy that applies to every business use case you might have in your enterprise application.

Once you have an EAGER association, there is no way you can make it LAZY. This way, the association will always be fetched even if the user does not necessarily need it for a particular use case. Even worse, if you forget to specify that an EAGER association needs to be JOIN FETCH-ed by a JPQL query, Hibernate is going to issue a secondary select for every uninitialized association, leading to N+1 query problems.

Unfortunately, JPA 1.0 decided that ```@ManyToOne``` and ```@OneToOne``` should default to ```FetchType.EAGER```, so now you have to explicitly mark these two associations as ```FetchType.LAZY```:

```
@ManyToOne(fetch = FetchType.LAZY)
private Post post;
```

##LAZY fetching
  
  For this reason, it’s better to use LAZY associations. A LAZY association is exposed via a Proxy, which allows the data access layer to load the association on demand. Unfortunately, LAZY associations can lead to ```LazyInitializationException```.
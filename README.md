# Lagom-Unmanaged-Service-With-Cassandra #

# Overview

  This coding challenge involves the development of a simple service that responds to a GET request, queries the DigitalOcean (DO) API, performs simple data transformations on the data received from DO, saves the data to a database(here,Cassandra), and finally sends a response.

# Documentation for DO API:

    https://status.digitalocean.com/api/v1#status (scroll down to the Components section)

  Please add whatever logging, telemetry, etc. that you think highlights your skills. This has deliberately been left open ended so that you have flexibility during development.

  Please use a language that runs on the JVM (ideally Java, Scala or Groovy). Use any framework (or not) that you want. If you don’t have a database setup, then you can just write the code (i.e. without an actual DB), use an in-memory DB, cloud DB, or whatever else you want. Overall, please pick the language and tools you’re comfortable with.

# Requirements

## Endpoint

1. GET /api/v1/components

  Create an API endpoint that accepts a GET request to /api/v1/components.

  If the endpoint is called without any filters, then query the following DO API (note: the DO API was selected because it does not require authentication):

    https://s2k7tnzlhrpw.statuspage.io/api/v1/components.json


2. GET /api/v1/components?name=

  Allow the caller to optionally specify a comma separated list of names, such as:

    GET /api/v1/components?name=API,Services

  If name is specified, then filter the JSON received from DO to only include components where component.name = <name specified in query string>.


## Validation

  Don’t worry about doing validation on every field. Validate the following fields as specified below:

    1. component.status

        Must be one of:

          operational
          degraded_performance
          partial_outage
          major_outage


    2. component.group_id

        Verify that group_id is not null. You will find some JSON component objects that have a null group_id,
        so remove those from the list.



## Filtering

If the API is called with the optional name query string, then only INCLUDE component objects that match the query string. For example, if the caller of your service does curl GET/api/v1/components?name=API , then remove all component objects where component.name != “API”.

## Transformation

In this step, we’ll do a very simple transformation of the data.

Concatenate the page_id and group_id into a single string field named composite_id
composite_id = a hash of (page_id + group_id)
Reduce the component object to the following fields:
status, name, composite_id

## Save to database

Use any database you want.

For each component that remains (i.e. the list of components after you’ve applied the filters and transformation above), save the status, name, and composite_id fields to a database.

## Response

Send a response that includes the number of component records saved to the database as:

{
	components: #count of the components saved#

}

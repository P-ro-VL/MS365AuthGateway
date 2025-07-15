package vn.neu.ms365authgateway.api;

@FunctionalInterface
public interface ApiCallExecutor<T> {
   ApiCallResult<T> call() throws ApiCallException;
}
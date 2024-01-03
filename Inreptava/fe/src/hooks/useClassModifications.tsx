import { useMutation } from "@tanstack/react-query";
import axiosClient from "../axios";
import SetMethodRequest from "../API/request/SetMethodRequest";

export function useClassModifications() {
    const { mutate, isPending, isError } = useMutation({
        mutationFn: (newTodo: SetMethodRequest) => {
          return axiosClient.post('/setMethod', newTodo)
        },
      })
  
    return {
        mutate,
        isPending,
        isError
    }
}


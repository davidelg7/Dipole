public class H1 extends Heuristics {

    public int eval(Board board, int player) {
        int evalFromI = evalfromI(board, player);
        int evalFromJ = evalfromJ(board, player);
        int evalToI = evalfromI(board, player);
        int evalToJ = evaltoJ(board, player);
        int evalN = evaln(board, player);
        return evalFromI+evalFromJ+evalToI+evalToJ+evalN;
    }
    public int evalfromI(Board board,int player){
        int sum=0;
        sum+=1;
        if(player <= 0.0){
            sum+=2;
            if(board.get(0,3) <= -1.5){
                sum+=3;
                if(board.get(0,3) <= -3.5){
                    sum=sum*sum;
                }
                else if(board.get(0,3) > -3.5){
                    sum=sum*sum;
                }
            }
            else if(board.get(0,3) > -1.5){
                sum+=3;
                if(board.get(4,3) <= -0.5){
                    sum=sum*sum;
                }
                else if(board.get(4,3) > -0.5){
                    sum=sum*sum;
                }
            }
        }
        else if(player > 0.0){
            sum+=2;
            if(board.get(7,4) <= 1.5){
                sum+=3;
                if(board.get(3,4) <= 0.5){
                    sum=sum*sum;
                }
                else if(board.get(3,4) > 0.5){
                    sum=sum*sum;
                }
            }
            else if(board.get(7,4) > 1.5){
                sum+=3;
                if(board.get(0,3) <= -4.5){
                    sum=sum*sum;
                }
                else if(board.get(0,3) > -4.5){
                    sum=sum*sum;
                }
            }
        }
        return sum;
    }
    public int evalfromJ(Board board,int player){
        int sum=0;
        sum+=1;
        if(player <= 0.0){
            sum+=2;
            if(board.get(0,3) <= -1.5){
                sum+=3;
                if(board.get(0,3) <= -3.5){
                    sum=sum*sum;
                }
                else if(board.get(0,3) > -3.5){
                    sum=sum*sum;
                }
            }
            else if(board.get(0,3) > -1.5){
                sum+=3;
                if(board.get(4,3) <= -0.5){
                    sum=sum*sum;
                }
                else if(board.get(4,3) > -0.5){
                    sum=sum*sum;
                }
            }
        }
        else if(player > 0.0){
            sum+=2;
            if(board.get(7,4) <= 1.5){
                sum+=3;
                if(board.get(3,4) <= 0.5){
                    sum=sum*sum;
                }
                else if(board.get(3,4) > 0.5){
                    sum=sum*sum;
                }
            }
            else if(board.get(7,4) > 1.5){
                sum+=3;
                if(board.get(0,3) <= -4.5){
                    sum=sum*sum;
                }
                else if(board.get(0,3) > -4.5){
                    sum=sum*sum;
                }
            }
        }
        return sum;
    }
    public int evaltoI(Board board,int player){
        int sum=0;
        sum+=1;
        if(player <= 0.0){
            sum+=2;
            if(board.get(0,3) <= -1.5){
                sum+=3;
                if(board.get(0,3) <= -3.5){
                    sum=sum*sum;
                }
                else if(board.get(0,3) > -3.5){
                    sum=sum*sum;
                }
            }
            else if(board.get(0,3) > -1.5){
                sum+=3;
                if(board.get(4,3) <= -0.5){
                    sum=sum*sum;
                }
                else if(board.get(4,3) > -0.5){
                    sum=sum*sum;
                }
            }
        }
        else if(player > 0.0){
            sum+=2;
            if(board.get(7,4) <= 1.5){
                sum+=3;
                if(board.get(3,4) <= 0.5){
                    sum=sum*sum;
                }
                else if(board.get(3,4) > 0.5){
                    sum=sum*sum;
                }
            }
            else if(board.get(7,4) > 1.5){
                sum+=3;
                if(board.get(0,3) <= -4.5){
                    sum=sum*sum;
                }
                else if(board.get(0,3) > -4.5){
                    sum=sum*sum;
                }
            }
        }
        return sum;
    }
    public int evaltoJ(Board board,int player){
        int sum=0;
        sum+=1;
        if(player <= 0.0){
            sum+=2;
            if(board.get(0,3) <= -1.5){
                sum+=3;
                if(board.get(0,3) <= -3.5){
                    sum=sum*sum;
                }
                else if(board.get(0,3) > -3.5){
                    sum=sum*sum;
                }
            }
            else if(board.get(0,3) > -1.5){
                sum+=3;
                if(board.get(4,3) <= -0.5){
                    sum=sum*sum;
                }
                else if(board.get(4,3) > -0.5){
                    sum=sum*sum;
                }
            }
        }
        else if(player > 0.0){
            sum+=2;
            if(board.get(7,4) <= 1.5){
                sum+=3;
                if(board.get(3,4) <= 0.5){
                    sum=sum*sum;
                }
                else if(board.get(3,4) > 0.5){
                    sum=sum*sum;
                }
            }
            else if(board.get(7,4) > 1.5){
                sum+=3;
                if(board.get(0,3) <= -4.5){
                    sum=sum*sum;
                }
                else if(board.get(0,3) > -4.5){
                    sum=sum*sum;
                }
            }
        }
        return sum;
    }
    public int evaln(Board board,int player){
        int sum=0;
        sum+=1;
        if(player <= 0.0){
            sum+=2;
            if(board.get(0,3) <= -1.5){
                sum+=3;
                if(board.get(0,3) <= -3.5){
                    sum=sum*sum;
                }
                else if(board.get(0,3) > -3.5){
                    sum=sum*sum;
                }
            }
            else if(board.get(0,3) > -1.5){
                sum+=3;
                if(board.get(4,3) <= -0.5){
                    sum=sum*sum;
                }
                else if(board.get(4,3) > -0.5){
                    sum=sum*sum;
                }
            }
        }
        else if(player > 0.0){
            sum+=2;
            if(board.get(7,4) <= 1.5){
                sum+=3;
                if(board.get(3,4) <= 0.5){
                    sum=sum*sum;
                }
                else if(board.get(3,4) > 0.5){
                    sum=sum*sum;
                }
            }
            else if(board.get(7,4) > 1.5){
                sum+=3;
                if(board.get(0,3) <= -4.5){
                    sum=sum*sum;
                }
                else if(board.get(0,3) > -4.5){
                    sum=sum*sum;
                }
            }
        }
        return sum;
    }
}
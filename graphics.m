function graphics

  bm = load('-ascii', 'bm.data');
  gp = load('-ascii', 'gp.data');
  ve = load('-ascii', 've.data');
  analytic = analytic(bm(:,1));
  % cuadraticError(analytic, bm(:,2))
  % cuadraticError(analytic, gp(:,2))
  % cuadraticError(analytic, ve(:,2))
  plot(bm(:,1), bm(:,2), 'color', 'r', ';Beeman;');
  hold on;
  plot(ve(:,1), ve(:,2), 'color', 'g', ';Verman;');
  hold on;
  plot(gp(:,1), gp(:,2), 'color', 'b', ';Gear Prediction;');
  hold on;
  plot(bm(:,1), analytic(:,1), 'color', 'm', ';Analítico;');
  xlabel('Tiempo (s)');
  ylabel('Posición (m)');
  h = legend('show');
end

function r = analytic(times)
  m = 70.0;
  gamma = 100.0;
  k = 10000.0;
  r = exp(-(gamma*times/(2*m))) .* cos(sqrt(k/m - gamma ** 2/(4 * (m ** 2)))*times);
end

% function error = cuadraticError(analytic, given)
%   error = analytic - given;
%   error = error .** 2;
%   error = sum(error) / size(error)(1);
% end
